package menu;



import menu.Recivers.ClientMenu;
import menu.Recivers.LoginMenu;
import org.client.FTPClient;
import org.client.FTPConnection;

import java.io.IOException;
import java.util.Scanner;

/**
 * CLIENT in Command pattern
 * Where pages with menu items inherited from Action are displayed. <br>
 * Works until the user selects the "Exit" menu item. <br>
 */
public class FTPSystem {
    /**
     * The flag denoting the operation of the system, if false - the program is terminated.
     */
    public static boolean isWork = true;

    /**
     * Displays all items on the page.
     */
    private static Controller controller;


    private static final Scanner scanner = new Scanner(System.in);


    public void start() {
        try {
            initSystem();
            controller.setPage(Page.clientPage(new ClientMenu()));

            while (isWork) {
                menu(controller);
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }finally {
            try {
                controller.getFtpClient().disconnect();
            } catch (Exception e) {
                System.err.println(e.getMessage());

            }
        }
    }


    private void initSystem() {
        LoginMenu loginMenu = new LoginMenu();
        FTPClient ftpClient = loginMenu.LogIn();
        FTPSystem.controller = new Controller(ftpClient);
        //if (true) throw new RuntimeException();
        //        Scanner scanner = new Scanner(System.in);
        //        scanner.nex
        //        controller = new Controller();
        //        Welcome_Menu = new WelcomeMenu();
        //        Manager_Menu = new ManagerMenu(controller);
    }

    private void menu(Controller controller) {
        controller.printPage();
        try {
            int command = Integer.parseInt(scanner.next());
            controller.executeCommand(command);
        } catch (RuntimeException e) {
            System.err.println("Wrong action\n");
        }
    }

}