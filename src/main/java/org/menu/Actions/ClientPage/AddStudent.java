package org.menu.Actions.ClientPage;

import org.menu.Actions.Action;
import org.menu.Controller;
import org.menu.Recivers.ClientMenu;

import java.util.Scanner;


public class AddStudent extends Action {
    private static final String title = "Add student";
    private final ClientMenu clientMenu;

    public AddStudent(int numberItem, ClientMenu clientMenu) {
        super(title, numberItem);
        this.clientMenu = clientMenu;
    }


    @Override
    public void execute(Controller controller) {
        String name = getNameFromConsole();
        clientMenu.addStudent(name,controller);
    }

    private String getNameFromConsole(){
        System.out.println("Enter the student name:");
        return  new Scanner(System.in).nextLine();
    }
}