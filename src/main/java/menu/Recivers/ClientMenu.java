package menu.Recivers;


import menu.Controller;
import org.model.Student;

import java.util.List;


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
    public void getStudentById(Controller controller, int id) {
        try {
            controller.getFtpClient().getFile(controller.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudent(String name, Controller controller) {
        //controller.getFtpClient().appendFile1();
    }

    public List<Student> getStudents(Controller controller, String name) {
       // controller.getFtpClient().getFile(controller.getFileName());
        return null;
    }
    public void removeStudent(Controller controller, int id){
      //  controller.getFtpClient()
    }
}