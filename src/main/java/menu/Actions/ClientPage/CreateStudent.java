package menu.Actions.ClientPage;

import menu.Actions.Action;
import menu.Controller;
import menu.Recivers.WelcomeMenu;

import java.util.Scanner;


public class CreateStudent extends Action {
    private static final String title = "Create a customer";
    private final WelcomeMenu welcomeMenu;

    public CreateStudent(int numberItem, WelcomeMenu welcomeMenu) {
        super(title, numberItem);
        this.welcomeMenu = welcomeMenu;
    }


    @Override
    public void execute(Controller controller) {
        String name = getNameFromConsole();
        welcomeMenu.createCustomer(name,controller);
    }

    private String getNameFromConsole(){
        System.out.println("Enter the customer name:");
        String name = new Scanner(System.in).nextLine();
        return  name;
    }
}
