package org.menu.Actions.ClientPage;


import org.menu.Actions.Action;
import org.menu.Controller;
import org.menu.Recivers.ClientMenu;
import org.model.Student;


import java.util.List;
import java.util.Scanner;

public class GetStudents extends Action {
    private static final String title = "Get students by name";

    private final ClientMenu clientMenu;

    public GetStudents(int numberItem, ClientMenu clientMenu) {
        super(title, numberItem);
        this.clientMenu = clientMenu;

    }

    @Override
    public void execute(Controller controller) {
        String nameFromConsole = getNameFromConsole();
        List<Student> studentList = clientMenu.getStudents(controller, nameFromConsole);

        if (studentList.isEmpty()) {
            System.err.println("Student not found");
        } else {
            this.printStudentsToConsole(studentList);
        }
    }

    private String getNameFromConsole() {
        System.out.println("Enter the student name:");
        return new Scanner(System.in).nextLine();
    }

    /**
     *  List students in alphabetical order.
     * @param students - List of students received from the server.
     */
    private void printStudentsToConsole(List<Student> students) {
        System.out.println();
        System.out.println("Students list:");
        students.stream().sorted().forEach(student -> System.out.println(">> " + student));
        System.out.println("End of list");
    }
}