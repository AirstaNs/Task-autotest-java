package menu.Actions.ClientPage;


import menu.Actions.Action;
import menu.Controller;
import menu.Recivers.ClientMenu;

import java.util.Scanner;

public class GetStudentById extends Action {
    private static final String title = "Getting a student by id";
    private final ClientMenu clientMenu;


    public GetStudentById(int numberItem, ClientMenu clientMenu) {
        super(title, numberItem);
        this.clientMenu = clientMenu;

    }
    @Override
    public void execute(Controller controller) {
        try {
            int id = getIdFromConsole();
            clientMenu.getStudentById(controller,id);

        } catch (Exception e) {
            System.out.println("Invalid input id");
        }
    }

    private int getIdFromConsole(){
        System.out.println("Enter the student id:");
        return  new Scanner(System.in).nextInt();
    }
}
