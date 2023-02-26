package org.client;


import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FTPClient  {
    public static final int DEFAULT_PORT = 21;
    private static final int DEFAULT_TIMEOUT = 7000;
    private Socket socket = null;
    private ServerSocket active = null;
    private BufferedReader readerResponse = null;
    private BufferedWriter writerRequest = null;
    private static boolean DEBUG = true; //TODO REMOVE
    private boolean isPassiveMode = false;


    public FTPClient(String host) throws IOException {
        if (Objects.isNull(host) || host.isEmpty()) throw new RuntimeException("host is empty");

        this.setSocket(new Socket(host, DEFAULT_PORT));
        String response = readResponse();
        if (!response.startsWith("220")) throw new RuntimeException("FTP server connection failed: " + response);
    }

    private synchronized void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.readerResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writerRequest = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        socket.setSoTimeout(DEFAULT_TIMEOUT);
    }

    public synchronized void login(String login, String pass) throws IOException {
        Objects.requireNonNull(login, "login cannot be empty");
        Objects.requireNonNull(pass, "pass cannot be empty");

            sendCommand(Command.USER, login);
            String response = readResponse();
            if (!response.startsWith("331")) throw new RuntimeException("FTP server invalid login: " + response);

            sendCommand(Command.PASS, pass);
            response = readResponse();
            if (!response.startsWith("230")) throw new RuntimeException("FTP server invalid password: " + response);
    }

    /* method to send commands to ftp server */
    private synchronized void sendCommand(Command command, String args) throws IOException {
        if (Objects.isNull(socket) | Objects.isNull(writerRequest)) {
            throw new NullPointerException("server not connected");
        }
        Objects.requireNonNull(command, "empty command");
        Objects.requireNonNull(args, "args null");

        final String endCommand = "\r\n";
        final String sep = " ";
        String commandLine = command.name() + sep + args + endCommand;

        writerRequest.write(commandLine); // "command args \r\n"
        writerRequest.flush();

        if (DEBUG) {
            if (command ==Command.USER || command ==Command.PASS) {
                final String invisible = "*****";
                commandLine = command.name() + sep + invisible + endCommand;
            }
            System.out.printf("\tsend -> %15s", commandLine);
        }
    }

    private synchronized String readResponse() throws IOException {
        Objects.requireNonNull(readerResponse, "server not connected");
        String response = readerResponse.readLine();
        while (readerResponse.ready()) {
            response = readerResponse.readLine();
        }
        if (DEBUG) System.out.printf("response -> %15s%n", response);
        return response;
    }

    public synchronized void disconnect() throws IOException, InterruptedException {
        try {
            sendCommand(Command.QUIT, "");
            Thread.sleep(500);
        } finally {
            Objects.requireNonNull(readerResponse, "server not connected");
            while (readerResponse.ready()) {
                System.out.println(readerResponse.readLine());
            }
            if (socket != null) socket.close();
            if (active != null) active.close();
            socket = null;
            active = null;
        }
    }


    public synchronized ServerSocket enterActiveMode() throws Exception {
        if (Objects.isNull(socket) | Objects.isNull(writerRequest)) {
            throw new NullPointerException("server not connected");
        }
            ServerSocket activeSocket = new ServerSocket(0);
            byte[] address = socket.getLocalAddress().getAddress();
            int port = activeSocket.getLocalPort();

            StringBuilder portCmd = new StringBuilder();
            char delimiter = ',';
            for (int i = 0; i < 4; i++) {
                portCmd.append(address[i] & 0xFF);
                portCmd.append(delimiter);
            }
            portCmd.append((port / 256)).append(delimiter).append(port % 256);

            sendCommand(Command.PORT, portCmd.toString());
           // if (active == null)
            String response = readResponse();// if 500 - not support active mode
            if(!response.startsWith("200")) throw new Exception("not request active mode: " + response);

            active = activeSocket;
            active.setSoTimeout(DEFAULT_TIMEOUT);
            return active;
    }

    public synchronized Socket enterPassiveMode() throws IOException, InterruptedException {

        sendCommand(Command.PASV, "");

        String response = readResponse();
        if (!response.startsWith("227 ")) throw new IOException("not request passive mode: " + response);

        Pattern compile = Pattern.compile("\\([\\d,]+\\)");
        Matcher matcher = compile.matcher(response);
        if (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            String[] split = response.substring(matchResult.start() + 1, matchResult.end() - 1).split(",");

            String ip = String.join(".", Arrays.copyOfRange(split, 0, 4));
            int port = (Integer.parseInt(split[4]) * 256) + Integer.parseInt(split[5]);
            isPassiveMode = true;
            return new Socket(ip, port);
        } else {
            throw new RuntimeException("not found ip");
        }

    }

    public void downloadFile(String fileName, Socket dataSocket) throws IOException {
          Objects.requireNonNull(dataSocket, "not connected");
        OutputStream fileOut = Files.newOutputStream(Paths.get("./files/" + fileName), StandardOpenOption.APPEND);
        try (BufferedInputStream input = new BufferedInputStream(dataSocket.getInputStream()); BufferedOutputStream output = new BufferedOutputStream(fileOut)) {
            byte[] buffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
        }
    }

    public synchronized void getFile(String fileName) throws Exception {
        Socket socket1 = setTransfer(fileName, Command.RETR);

        String response = readResponse();
        boolean accept = !response.startsWith("150") && !response.startsWith("125") && !response.startsWith("226");
        if (accept){
            String notFound = "could not be found on the FTP server.";
            String invalid = "Invalid response from FTP server for file transfer: " + response;
            String messErr = response.startsWith("550") ? notFound : invalid;
            throw new RuntimeException(messErr);
        }
        downloadFile(fileName, socket1);
        readResponse();
    }

    private synchronized Socket setTransfer(String fileName, Command command) throws Exception {
        Socket thisSocket;
        if (!isPassiveMode) {
            ServerSocket serverSocket = enterActiveMode();
            sendCommand(command, fileName);
            thisSocket = Objects.requireNonNull(serverSocket).accept();
        } else {
            thisSocket = this.enterPassiveMode();
            sendCommand(command, fileName);
        }
        return thisSocket;
    }

    private synchronized Socket setTransfer(Command command) throws Exception {
        Socket thisSocket;
        if (!isPassiveMode) {
            ServerSocket serverSocket = enterActiveMode();
            sendCommand(command, "");
            thisSocket = Objects.requireNonNull(serverSocket).accept();
        } else {
            thisSocket = this.enterPassiveMode();
            sendCommand(command, "");
        }
        return thisSocket;
    }


    public synchronized void appendFile1(String fileName) throws Exception {
        Socket socket1 = setTransfer(fileName, Command.APPE);
        uploadFile((fileName), socket1);
        Thread.sleep(600);
        readResponse();

    }

    public void uploadFile(String localFilename, Socket dataSocket) throws IOException {
        Objects.requireNonNull(dataSocket, "not connected");
        try (BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
             BufferedInputStream input = new BufferedInputStream(Files.newInputStream(Paths.get(
                "./files/" + localFilename)))) {
            byte[] buffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
        }
    }

    public boolean findFile(String remoteName) throws Exception {
        boolean isFind = false;
        try (Socket socket1 = setTransfer(Command.LIST)) {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(socket1.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    if (line.contains(remoteName)) {
                        isFind = true;
                        break;
                    }
                }
                readResponse();
            }
        }
        return isFind;
    }
}
