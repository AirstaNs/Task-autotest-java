package SocketImpl;


import java.io.*;
import java.net.Socket;
import java.util.Objects;


public class Server {
    private Socket socket = null;

    private BufferedReader reader = null;

    private BufferedWriter writer = null;

    private static boolean DEBUG = true; //TODO REMOVE

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
