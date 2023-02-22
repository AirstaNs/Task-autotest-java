package menu.Actions.ClientPage;


import menu.Actions.Action;
import menu.Controller;
import menu.Recivers.WelcomeMenu;
import org.example.Student;


import java.util.List;

public class GetStudents extends Action {
    private static final String title = "Получение списка студентов по имени";

    private final WelcomeMenu welcomeMenu;

    public GetStudents(int numberItem, WelcomeMenu welcomeMenu) {
        super(title, numberItem);
        this.welcomeMenu = welcomeMenu;

    }


    @Override
    public void execute(Controller controller) {
        List<Student> customerList = welcomeMenu.GetStudents(controller);

        if (customerList.isEmpty()) {
            System.out.println("The customer list is empty!");
        } else {
            this.printComaniesToConsole(customerList);
        }
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
