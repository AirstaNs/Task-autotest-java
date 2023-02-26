package menu.Actions.ClientPage;


import menu.Actions.Action;
import menu.Controller;
import menu.Recivers.ClientMenu;
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
            System.out.println("Student not found");
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
        System.out.println("Students list:");
        students.stream()
                .sorted()
                .forEach(student -> System.out.println("--"+student));
        System.out.println("End of list\n");
       // students.forEach((student) -> System.out.printf("%d. %s%n", student.getId(), student.getName()));
            /* TODO
           for (int i = 0; i < students.size(); i++) {
                Car car = students.get(i);
                System.out.printf("%d. %s%n", i+1, car.getName());
            }
             */
    }
}