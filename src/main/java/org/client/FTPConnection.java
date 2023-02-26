package org.client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FTPConnection {
    private static boolean DEBUG = false;
    public static final int DEFAULT_PORT = 21;
    private static final int DEFAULT_TIMEOUT = 7000;
    private Socket socket = null;
    private BufferedReader readerResponse = null;
    private BufferedWriter writerRequest = null;

    private boolean  isPassiveMode = true;

    public FTPConnection(String host) throws IOException {
        if (Objects.isNull(host) || host.isEmpty()) throw new RuntimeException("host is empty");

        this.setSocket(new Socket(host, DEFAULT_PORT));
        String response = readResponse();
        if (Objects.isNull(response)|| !response.startsWith("220")) throw new RuntimeException("FTP server connection failed");
    }
    private synchronized void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.readerResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writerRequest = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        socket.setSoTimeout(DEFAULT_TIMEOUT);
    }

    protected synchronized String readResponse() throws IOException {
        Objects.requireNonNull(readerResponse, "server not connected");
        String response = readerResponse.readLine();
        while (readerResponse.ready()) {
            response = readerResponse.readLine();
        }
        if (DEBUG) System.out.printf("response -> %15s%n", response);
        return response;
    }
    /* method to send commands to ftp server */
    protected synchronized void sendCommand(Command command, String args) throws IOException {
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

    public synchronized void disconnect() throws IOException, InterruptedException {
        try {
            this.sendCommand(Command.QUIT, "");
            Thread.sleep(500);
        } finally {
            Objects.requireNonNull(readerResponse, "server not connected");
            while (readerResponse.ready()) {
                System.out.println(readerResponse.readLine());
            }
            if (socket != null) socket.close();
            socket = null;
        }
    }

    protected synchronized Socket setTransfer(String fileName, Command command) throws Exception {
        Socket thisSocket;
        try {
            if (!isPassiveMode) {
                ServerSocket serverSocket = enterActiveMode();
                sendCommand(command, fileName);
                thisSocket = Objects.requireNonNull(serverSocket).accept();
            } else {
                thisSocket = this.enterPassiveMode();
                sendCommand(command, fileName);
            }
        }catch (Exception e){
            ServerSocket serverSocket = enterActiveMode();
            sendCommand(command, fileName);
            thisSocket = Objects.requireNonNull(serverSocket).accept();
            isPassiveMode = false;
        }
        return thisSocket;
    }

    protected synchronized Socket setTransfer(Command command) throws Exception {
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

    private synchronized ServerSocket enterActiveMode() throws Exception {
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

        activeSocket.setSoTimeout(DEFAULT_TIMEOUT);
        return activeSocket;
    }

    private synchronized Socket enterPassiveMode() throws IOException, InterruptedException {

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

}
