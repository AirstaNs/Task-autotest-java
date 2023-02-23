package SocketImpl;


import java.io.*;
import java.net.Socket;
import java.util.Objects;


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
        if (Objects.isNull(login)) throw new RuntimeException("login cannot be empty");
        if (Objects.isNull(pass)) throw new RuntimeException("pass cannot be empty");
        try {
            sendCommand(Command.USER, login);
            String response = readResponse();
            if (!response.startsWith("331 ")) throw new RuntimeException("FTP server invalid login: " + response);
            sendCommand(Command.PASS, pass);
            response = readResponse();
            if (!response.startsWith("230 ")) throw new RuntimeException("FTP server invalid password: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* method to send commands to ftp server */
    private void sendCommand(Command command, String args) throws IOException {
        if (Objects.isNull(command)) throw new RuntimeException("command null");
        if (Objects.isNull(args)) throw new RuntimeException("args null");
        if (Objects.isNull(socket) | Objects.isNull(writer)) throw new RuntimeException("server not connected");
        final String endCommand = "\r\n";
        final String sep = " ";

        String commandLine = command.name() + sep + args + endCommand;
        writer.write(commandLine); // "command args \r\n"
        writer.flush();
        if (DEBUG) System.out.printf("send command -> %s %n", commandLine);
    }

    private String readResponse() throws IOException {
        String response = reader.readLine();
        if (DEBUG) {
            System.out.printf("response -> %s %n", response);
        }
        return response;
    }
}
