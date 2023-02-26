package menu.Actions.ClientPage;


import menu.Actions.Action;
import menu.Controller;
import menu.Recivers.ClientMenu;
import org.model.Student;

import java.util.Optional;
import java.util.Scanner;

public class GetStudentById extends Action {
    private static final String title = "Get student by id";
    private final ClientMenu clientMenu;


    public GetStudentById(int numberItem, ClientMenu clientMenu) {
        super(title, numberItem);
        this.clientMenu = clientMenu;

    }

    @Override
    public void execute(Controller controller) {
        try {
            int id = getIdFromConsole();
            Optional<Student> studentById = clientMenu.getStudentById(controller, id);
            if(studentById.isPresent())
                System.out.println(studentById.get());
            else System.err.println("Student not found");
        } catch (Exception e) {
            System.err.println("Invalid input id");
        }
    }

    private int getIdFromConsole() {
        System.out.println("Enter the student id:");
        return new Scanner(System.in).nextInt();
    }
}
