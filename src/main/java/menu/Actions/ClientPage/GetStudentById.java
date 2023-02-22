package menu.Actions.ClientPage;


import menu.Actions.Action;
import menu.Controller;
import menu.Recivers.WelcomeMenu;

public class GetStudentById extends Action {
    private static final String title = "Получение информации о студенте по id";
    private final WelcomeMenu welcomeMenu;


    public GetStudentById(int numberItem, WelcomeMenu welcomeMenu) {
        super(title, numberItem);
        this.welcomeMenu = welcomeMenu;

    }


    @Override
    public void execute(Controller controller) {
        welcomeMenu.GetStudentById(controller);
    }
}
