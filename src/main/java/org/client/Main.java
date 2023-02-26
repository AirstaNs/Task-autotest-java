package org.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        FTPClient FTPClient = null;
        try {
            String name = "students.json";
            FTPClient = getServer();
            FTPClient.getFile(name);
            FTPClient.appendFile1(name);
            System.out.println(FTPClient.findFile(name));
        }catch (Exception e){
            e.printStackTrace();
        }finally
        {
            if (FTPClient != null) {
                try {
                    FTPClient.disconnect();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static FTPClient getServer() {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("./hosts/beget.txt"))) {
            String host = bufferedReader.readLine();
            String log = bufferedReader.readLine();
            String pass = bufferedReader.readLine();
            FTPConnection FTPConnection = new FTPConnection(host);
            FTPClient FTPClient = new FTPClient(FTPConnection);
            FTPClient.login(log, pass);
            return FTPClient;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}