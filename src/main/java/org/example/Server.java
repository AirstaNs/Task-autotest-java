package org.example;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;

/**
 * A class that stores data to establish a connection with the server.
 */
public class Server {
    public static final int DEFAULT_HOST = 21;
    private final String login;
    private final String pass;
    private final String host;
    private final URL link;

    public Server(String login, String pass, String host) throws MalformedURLException {
        this.login = login;
        this.pass = pass;
        this.host = host;
        String url = String.format("ftp://%s:%s@%s:%d/students.json;type=i", login, this.getPass(), host, Server.DEFAULT_HOST);
        this.link = new URL(url);

    }


    public String download(String localFile) {
        try (BufferedInputStream is = new BufferedInputStream(link.openConnection().getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(localFile, true)) {
            int i;
            while ((i = is.read()) != -1) {
                fileOutputStream.write(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String upload(String localFile) throws IOException {
        URLConnection url = link.openConnection();
        url.setDoOutput(true);
        try (BufferedOutputStream out = new BufferedOutputStream(url.getOutputStream())) {
            byte[] bytes = Files.readAllBytes(Paths.get(localFile));
            out.write(bytes);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getHost() {
        return host;
    }
}
