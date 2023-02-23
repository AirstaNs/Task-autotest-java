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
    private Socket socket = null;

    private BufferedReader reader = null;

    private BufferedWriter writer = null;

    private static boolean DEBUG = true; //TODO REMOVE

    public Server(String host) {
        if (Objects.isNull(host) || host.isEmpty()) throw new RuntimeException("host is empty");
        try {
            socket = new Socket(host, DEFAULT_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String response = readResponse();
            if (!response.startsWith("220 ")) throw new RuntimeException("FTP server connection failed: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String login, String pass) {
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
    private void sendCommand(Command command, String args) throws IOException {
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


    private String readResponse() throws IOException {
        Objects.requireNonNull(reader, "server not connected");
        String response = reader.readLine();
        while (reader.ready()) {
            response = reader.readLine();
        }
        if (DEBUG) System.out.printf("response -> %15s%n", response);
        return response;
    }

    public synchronized void disconnect() throws IOException {
        try {
            sendCommand(Command.QUIT, "");
        } finally {
            readResponse();
            socket = null;
        }
    }

    public void enterActiveMode() throws IOException {
        if (Objects.isNull(socket) | Objects.isNull(writer)) throw new NullPointerException("server not connected");
        try (ServerSocket activeSocket = new ServerSocket(0)) {
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
        }
    }

    public void enterPassiveMode() throws IOException {

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
        } else {
            throw new RuntimeException("not found ip");
        }
        if (true) throw new RuntimeException("fix");

        //TODO     Socket dataSocket = new Socket(ip, port);

    }

}
