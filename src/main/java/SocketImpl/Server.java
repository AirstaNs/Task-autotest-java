package SocketImpl;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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




    public synchronized void enterActiveMode() throws IOException {
        if (Objects.isNull(socket) | Objects.isNull(writer)) throw new NullPointerException("server not connected");
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

            String response = readResponse(); // if 500 - not support active mode

            active = activeSocket;
            active.setSoTimeout(DEFAULT_TIMEOUT);
        } catch (Exception e) {

        }
    }

    public synchronized void enterPassiveMode() throws IOException, InterruptedException {

        sendCommand(Command.PASV, "");

        String response = readResponse();
        if (!response.startsWith("227")) throw new IOException("not request passive mode: " + response);

        Pattern compile = Pattern.compile("\\([\\d,]+\\)");
        Matcher matcher = compile.matcher(response);
        if (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            String[] split = response.substring(matchResult.start() + 1, matchResult.end() - 1).split(",");

            String ip = String.join(".", Arrays.copyOfRange(split, 0, 4));
            int port = (Integer.parseInt(split[4]) * 256) + Integer.parseInt(split[5]);
            this.setSocket(new Socket(ip, port));
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


    }
}
