package menu.Recivers;


import menu.Controller;
import org.example.Student;

import java.util.List;


/**
 * RECEIVER  in Command pattern
 * <br>
 * The initialization page is available without logging in.
 * A specific menu item and page, each method describes a specific action on the page.
 */

public class WelcomeMenu implements ShouldBeExit {

    /**
     * The action of signing in to an account. <br>
     * Reads the login data from the console: Card Number and Pin. <br>
     * Takes from the context of the User. <br>
     * If the data is incorrect - DOES NOT redirect to the page of the Personal Account. <br>
     * if correct, redirects to the page of the Personal Account
     */
    public void GetStudentById(Controller controller) {

    }

    public void createCustomer(String name, Controller controller) {
     //   controller.getContext().customerDAO.create(name);
    }

    public List<Student> GetStudents(Controller controller) {
       // var customer = Main.download(controller.getServer());
        //return customer.orElseThrow(() -> new RuntimeException("No Customer"));
        return null;
    }
}