package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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


    public String download() {
        try (InputStream is = link.openConnection().getInputStream()) {
            Path path = Paths.get("students.json");
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String upload() throws IOException {
        URLConnection url = link.openConnection();
        url.setDoOutput(true);
        try (OutputStream is1 = url.getOutputStream()) {
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
