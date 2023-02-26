package menu.Recivers;


import menu.Controller;
import org.client.FTPClient;
import org.client.FTPConnection;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;


public class LoginMenu implements ShouldBeExit {

    public FTPClient LogIn() {
        FTPClient ftpClient = null;
        Scanner scanner = new Scanner(System.in);
        boolean success = false;
        System.out.println("Welcome to the console FTPClient");
        while (!success) {
            Optional<FTPConnection> host = initHost(scanner);
            if(!host.isPresent()) continue;

            ftpClient = new FTPClient(host.get());
            success = login(ftpClient, scanner);
        }
        System.out.println("Login successful");
        return ftpClient;
    }

    private Optional<FTPConnection> initHost(Scanner scanner) {
        Optional<FTPConnection> empty = Optional.empty();
        System.out.println("Enter server IP-address: ");
        try {
            String host = scanner.next();
            return Optional.of(new FTPConnection(host));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Connection error");
        }
        System.out.println("Please try again");
        return empty;
    }

    private boolean login(FTPClient ftpClient, Scanner scanner) {
        boolean successful = false;
        System.out.println("Enter login: ");
        String login = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();
        try {
            ftpClient.login(login, password);
            successful = true;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Connection error");
        }
        return successful;
    }


    public static void main(String[] args) throws Exception {
        FTPClient ftpClient = new LoginMenu().LogIn();
        System.out.println(ftpClient.getFile("students.json"));
    }
}
