package org.client;


import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;


public class FTPClient {
    private final FTPConnection ftpConnection;

    public void disconnect() throws IOException, InterruptedException {
        ftpConnection.disconnect();
    }

    public FTPClient(FTPConnection ftpConnection) throws IOException {
        this.ftpConnection = ftpConnection;
    }

    public synchronized void login(String login, String pass) throws IOException {
        Objects.requireNonNull(login, "login cannot be empty");
        Objects.requireNonNull(pass, "pass cannot be empty");

        ftpConnection.sendCommand(Command.USER, login);
        String response = ftpConnection.readResponse();
        if (!response.startsWith("331")) throw new RuntimeException("FTP server invalid login: " + response);

        ftpConnection.sendCommand(Command.PASS, pass);
        response = ftpConnection.readResponse();
        if (!response.startsWith("230")) throw new RuntimeException("FTP server invalid password: " + response);
    }


    private void downloadFile(String fileName, Socket dataSocket) throws IOException {
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
        Socket socket1 = ftpConnection.setTransfer(fileName, Command.RETR);

        String response = ftpConnection.readResponse();
        boolean accept = !response.startsWith("150") && !response.startsWith("125") && !response.startsWith("226");
        if (accept) {
            String notFound = "could not be found on the FTP server.";
            String invalid = "Invalid response from FTP server for file transfer: " + response;
            String messErr = response.startsWith("550") ? notFound : invalid;
            throw new RuntimeException(messErr);
        }
        downloadFile(fileName, socket1);
        ftpConnection.readResponse();
    }

    public synchronized void appendFile1(String fileName) throws Exception {
        Socket socket1 = ftpConnection.setTransfer(fileName, Command.APPE);
        uploadFile((fileName), socket1);
        Thread.sleep(600);
        ftpConnection.readResponse();

    }

    private void uploadFile(String localFilename, Socket dataSocket) throws IOException {
        Objects.requireNonNull(dataSocket, "not connected");
        try (BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream()); BufferedInputStream input = new BufferedInputStream(Files.newInputStream(Paths.get(
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
        try (Socket socket1 = ftpConnection.setTransfer(Command.LIST)) {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(socket1.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    if (line.contains(remoteName)) {
                        isFind = true;
                        break;
                    }
                }
                ftpConnection.readResponse();
            }
        }
        return isFind;
    }
}
