package org.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = getServer();
        server.download("files/students.json");
        server.upload("files/students.json");
    }

    private static Server getServer() {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("./hosts/beget.txt"))) {
            String host = bufferedReader.readLine();
            String log = bufferedReader.readLine();
            String pass = bufferedReader.readLine();
            return new Server(log, pass, host);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}