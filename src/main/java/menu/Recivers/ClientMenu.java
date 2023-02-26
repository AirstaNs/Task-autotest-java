package menu.Recivers;


import menu.Controller;
import org.model.Student;
import org.parser.JSON;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * RECEIVER  in Command pattern
 * <br>
 * The initialization page is available without logging in.
 * A specific menu item and page, each method describes a specific action on the page.
 */

public class ClientMenu implements ShouldBeExit {

    /**
     * The action of signing in to an account. <br>
     * Reads the login data from the console: Card Number and Pin. <br>
     * Takes from the context of the User. <br>
     * If the data is incorrect - DOES NOT redirect to the page of the Personal Account. <br>
     * if correct, redirects to the page of the Personal Account
     */
    public Optional<Student> getStudentById(Controller controller, int id) {
        Optional<Student> optStudent = Optional.empty();
        try {
            optStudent = getListStudents(controller).stream().filter(student -> student.getId() == id).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optStudent;
    }

    public void addStudent(String name, Controller controller) {
        //controller.getFtpClient().appendFile1();
    }

    public List<Student> getStudents(Controller controller, String name) {
        List<Student> students = Collections.emptyList();
        try {
            students = getListStudents(controller)
                    .stream()
                    .filter(student -> student.getName().equals(name))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public void removeStudent(Controller controller, int id) throws Exception {
        List<Student> listStudents = getListStudents(controller);
        boolean b = listStudents.removeIf(student -> student.getId() == id);
        if (b) {
            JSON json = new JSON();
            String jsonStr = json.toJson(listStudents);
            controller.getFtpClient().replaceFile(controller.getFileName(), jsonStr);
        }
    }

    private List<Student> getListStudents(Controller controller) throws Exception {
        String file = controller.getFtpClient().getFile(controller.getFileName());
        JSON json = new JSON();
        return json.fromJson(file);
    }
}