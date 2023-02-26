package menu.Actions.ClientPage;


import menu.Actions.Action;
import menu.Controller;
import menu.Recivers.ClientMenu;
import org.model.Student;


import java.util.List;
import java.util.Scanner;

public class GetStudents extends Action {
    private static final String title = "Getting students by name";

    private final ClientMenu clientMenu;

    public GetStudents(int numberItem, ClientMenu clientMenu) {
        super(title, numberItem);
        this.clientMenu = clientMenu;

    }


    @Override
    public void execute(Controller controller) {
            String nameFromConsole = getNameFromConsole();

        List<Student> customerList = clientMenu.getStudents(controller,nameFromConsole);

        if (customerList.isEmpty()) {
            System.out.println("The student list is empty!");
        } else {
            this.printComaniesToConsole(customerList);
        }

    }

    private String getNameFromConsole() {
        System.out.println("Enter the student name:");
        return new Scanner(System.in).nextLine();
    }

    private boolean isBackAction(int actionOrCompany) {
        return actionOrCompany == 0;
    }

    private void printComaniesToConsole(List<Student> students) {
        System.out.println("Customer list:");
        students.forEach((student) -> System.out.printf("%d. %s%n", student.getId(), student.getName()));
            /* TODO
           for (int i = 0; i < students.size(); i++) {
                Car car = students.get(i);
                System.out.printf("%d. %s%n", i+1, car.getName());
            }
             */
    }
}