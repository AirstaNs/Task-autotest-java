package menu.Actions.ClientPage;


import menu.Actions.Action;
import menu.Controller;
import menu.Recivers.ClientMenu;

public class GetStudentById extends Action {
    private static final String title = "Getting a student by id";
    private final ClientMenu clientMenu;


    public GetStudentById(int numberItem, ClientMenu clientMenu) {
        super(title, numberItem);
        this.clientMenu = clientMenu;

    }


    @Override
    public void execute(Controller controller) {
        clientMenu.getStudentById(controller);
    }
}
