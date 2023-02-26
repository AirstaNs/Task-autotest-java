package menu;



import menu.Recivers.ClientMenu;
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
        initSystem();

        //ontroller.setPage(Page.clientPage(Welcome_Menu));
        controller.setPage(Page.clientPage(new ClientMenu()));

        while (isWork) {
            menu(controller);
        }
    }


    private void initSystem() {

        try {
            FTPSystem.controller = new Controller(new FTPClient(new FTPConnection(null)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            controller.executeCommand(scanner.nextInt());
        } catch (RuntimeException e) {
            System.out.println("Wrong action\n");
        }
    }

}