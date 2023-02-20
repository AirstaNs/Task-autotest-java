package org.example;


import java.io.*;
import java.net.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
    }

    public static String download(String login, String pass, String host, int port) {
        String url1 = String.format("ftp://%s:%s@%s:%d/students.json;type=i", login, pass, host, port);
        try (InputStream is = new URL(url1).openConnection().getInputStream()) {
            Path path = Paths.get("students.json");
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String upload(String login, String pass, String host, int port) throws IOException {
        String url1 = String.format("ftp://%s:%s@%s:%d/students.txt;type=a+", login, pass, host, port);
        URLConnection is = new URL(url1).openConnection();
        is.setDoOutput(true);
        try (OutputStream is1 = is.getOutputStream()) {
            String str = "123";
            OutputStreamWriter osw = new OutputStreamWriter(is1);
            for (int i = 0; i < str.length(); i++) {
                osw.append(str.charAt(i));
                //  osw.write((int) str.charAt(i));
            }
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}