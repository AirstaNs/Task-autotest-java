package SocketImpl;


import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Server {
    public static final int DEFAULT_PORT = 21;
    private static final int DEFAULT_TIMEOUT = 7000;

    private Socket socket = null;

    private ServerSocket active = null;

    private BufferedReader reader = null;

    private BufferedWriter writer = null;

    private static boolean DEBUG = true; //TODO REMOVE

    public Server(String host) {
        if (Objects.isNull(host) || host.isEmpty()) throw new RuntimeException("host is empty");
        try {
            this.setSocket(new Socket(host, DEFAULT_PORT));
            String response = readResponse();
            if (!response.startsWith("220 ")) throw new RuntimeException("FTP server connection failed: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        socket.setSoTimeout(DEFAULT_TIMEOUT);
    }

    public synchronized void login(String login, String pass) {
        Objects.requireNonNull(login, "login cannot be empty");
        Objects.requireNonNull(pass, "pass cannot be empty");

        try {
            sendCommand(Command.USER, login);
            String response = readResponse();
            if (!response.startsWith("331")) throw new RuntimeException("FTP server invalid login: " + response);

            sendCommand(Command.PASS, pass);
            response = readResponse();
            if (!response.startsWith("230")) throw new RuntimeException("FTP server invalid password: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* method to send commands to ftp server */
    private synchronized void sendCommand(Command command, String args) throws IOException {
        if (Objects.isNull(socket) | Objects.isNull(writer)) throw new NullPointerException("server not connected");
        Objects.requireNonNull(command, "empty command");
        Objects.requireNonNull(args, "args null");

        final String endCommand = "\r\n";
        final String sep = " ";
        String commandLine = command.name() + sep + args + endCommand;

        writer.write(commandLine); // "command args \r\n"
        writer.flush();

        if (DEBUG) {
            if (command.equals(Command.USER) || command.equals(Command.PASS)) {
                final String invisible = "*****";
                commandLine = command.name() + sep + invisible + endCommand;
            }
            System.out.printf("\tsend -> %15s", commandLine);
        }
    }

    private synchronized String readResponse() throws IOException, InterruptedException {
        Objects.requireNonNull(reader, "server not connected");
        String response = reader.readLine();
        while (reader.ready()) {
            response = reader.readLine();
        }
        if (DEBUG) System.out.printf("response -> %15s%n", response);
        return response;
    }

    public synchronized void disconnect() throws IOException, InterruptedException {
        try {
            sendCommand(Command.QUIT, "");
            Thread.sleep(500);
        } finally {
            Objects.requireNonNull(reader, "server not connected");
            while (reader.ready()) {
                System.out.println(reader.readLine());
                Thread.sleep(500);
            }
            if (socket != null) socket.close();
            if (active != null) active.close();
            socket = null;
            active = null;
        }
    }


    public synchronized ServerSocket enterActiveMode() throws IOException {
        if (Objects.isNull(socket) | Objects.isNull(writerRequest)) {
            throw new NullPointerException("server not connected");
        }
        try {
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
            if (active == null) readResponse(); // if 500 - not support active mode

            active = activeSocket;
            active.setSoTimeout(DEFAULT_TIMEOUT);
            return active;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            //  this.setSocket();
            isPassiveMode = true;
            return new Socket(ip, port);
        } else {
            throw new RuntimeException("not found ip");
        }
        if (true) throw new RuntimeException("fix");

        //TODO     Socket dataSocket = new Socket(ip, port);

    }

    public void downloadFile(String fileName) {
        try {
            Socket accept = active.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            try (FileWriter writer1 = new FileWriter("./files/" + fileName, true)) {
                int byteCount;
                char[] bytes = new char[512];
                while ((byteCount = in.read(bytes, 0, 512)) != -1) {
                    writer1.write(bytes, 0, byteCount);
                }
                writer1.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void getFile(String fileName) throws IOException, InterruptedException {
        this.enterActiveMode();

        sendCommand(Command.RETR, fileName);

        String response = readResponse();

        if (!response.startsWith("150")) {
            String notFound = "could not be found on the FTP server.";
            String invalid = "Invalid response from FTP server for file transfer: " + response;
            String messErr = response.startsWith("550") ? notFound : invalid;
            throw new RuntimeException(messErr);
        }

        downloadFile(fileName);
        response = readResponse();

    private synchronized Socket setTransfer(String fileName, Command command) throws IOException, InterruptedException {
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

    private synchronized Socket setTransfer(Command command) throws IOException, InterruptedException {
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


    public synchronized void appendFile1(String fileName) throws IOException, InterruptedException {
        enterActiveMode();

        sendCommand(Command.APPE, "Aboba.txt"); //STOR - ГРУЗИТЬ БЕЗ . APPE - ДОБАВЛЯТЬ
        readResponse();
        uploadFile(("./files/" + fileName), active.accept());
        readResponse();

    }

    public void uploadFile(String localFilename, Socket dataSocket) throws IOException {
        Objects.requireNonNull(dataSocket, "not connected");

        try (BufferedOutputStream out = new BufferedOutputStream(dataSocket.getOutputStream()); FileInputStream in = new FileInputStream(localFilename)) {

            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.flush();
        }
    }

    public boolean findFile(String remoteName) throws IOException, InterruptedException {
        enterActiveMode();
        sendCommand(Command.LIST, "");

        Socket activeSocket = active.accept();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(activeSocket.getInputStream()))) {
            boolean isFind = true;
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains(remoteName)) return isFind;
            }
        }
        readResponse();
        return false;
    }
}
