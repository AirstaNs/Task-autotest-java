package org.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = null;
        try {
            String name = "students.json";
            server = getServer();
            server.getFile(name);
            server.appendFile1(name);
            System.out.println(server.findFile(name));
        }catch (Exception e){
            e.printStackTrace();
        }finally
        {
            if (server != null) {
                server.disconnect();
            }
        }
    }

    public static Server getServer() {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("./hosts/beget.txt"))) {
            String host = bufferedReader.readLine();
            String log = bufferedReader.readLine();
            String pass = bufferedReader.readLine();
            Server server = new Server(host);
            server.login(log, pass);
            return server;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}