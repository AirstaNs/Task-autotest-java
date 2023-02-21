package org.example;

/**
 * A class that stores data to establish a connection with the server.
 */
public class Server {
    public static final int DEFAULT_HOST = 21;
    private final String login;
    private final char[] pass;
    private final String host;

    public Server(String login, char[] pass, String host) {
        this.login = login;
        this.pass = pass;
        this.host = host;
    }

    public String getLogin() {
        return login;
    }

    public char[] getPass() {
        return pass;
    }

    public String getHost() {
        return host;
    }
}
