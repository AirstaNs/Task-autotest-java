package menu;



import menu.Recivers.ClientMenu;

import java.util.Scanner;

/**
 * CLIENT in Command pattern
 * A class that simulates the operation of the banking system. <br>
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
    /**
     * The initialization page is available without logging in. A specific menu item and page
     */
    public static ClientMenu Welcome_Menu;

    /**
     * The personal account page in the system is available after logging in.
     */
 //   public static ManagerMenu Manager_Menu;

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Launching the banking system, exposes the welcome page - loginMenu. <br>
     * Works as long as {@link FTPSystem#isWork}  != false.
     */
    public void start(String nameDB) {
        initSystem(nameDB);

        controller.setPage(Page.welcomePage(Welcome_Menu));


        while (isWork) {
            menu(controller);
        }
    }

    /**
     * Initializing the Context, Controller and Menu Pages. <br>
     * Setting the UID counter from the database.
     * @param nameDB - the name of the database to be accessed
     */
    private void initSystem(String nameDB) {
        if (true) throw new RuntimeException();
        //        Scanner scanner = new Scanner(System.in);
        //        scanner.nex
        //        controller = new Controller();
        //        Welcome_Menu = new WelcomeMenu();
        //        Manager_Menu = new ManagerMenu(controller);
    }

    /**
     * Prints menu items from the controller, sets actions, from the number read from the console
     * @param controller {@link FTPSystem#controller}
     */
    private void menu(Controller controller) {
        controller.printPage();
        try {
            controller.executeCommand(scanner.nextInt());
        } catch (RuntimeException e) {
            System.out.println("Wrong action\n");
        }
    }

}