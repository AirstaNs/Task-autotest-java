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
        final String endCommand = "\r\n";
        final String sep = " ";
        String commandLine = command.name() + sep + args + endCommand;
        writer.write(commandLine); // "command args \r\n"
        writer.flush();
        if (DEBUG) {
            System.out.printf("send command -> %s %n", commandLine);
        }
    }
}
