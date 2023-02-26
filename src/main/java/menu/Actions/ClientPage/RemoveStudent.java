package menu.Actions.ClientPage;

import menu.Actions.Action;
import menu.Controller;
import menu.Recivers.ClientMenu;

import java.util.Scanner;

public class RemoveStudent extends Action {
    private static final String title = "remove Student";
    private final ClientMenu clientMenu;

    public RemoveStudent(int numberItem, ClientMenu clientMenu) {
        super(title, numberItem);
        this.clientMenu = clientMenu;
    }

    @Override
    public void execute(Controller controller) {
        try {
            int id = getIdFromConsole();
            clientMenu.removeStudent(controller, id);

        } catch (RuntimeException e) {
            System.out.println("Invalid input id");
        } catch (Exception e) {
            System.out.println("Failed to remove student");
        }
    }

    private int getIdFromConsole(){
        System.out.println("Enter the student id:");
        return  new Scanner(System.in).nextInt();
    }
}
