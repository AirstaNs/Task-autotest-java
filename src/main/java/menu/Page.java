package menu;


import menu.Actions.Action;
import menu.Actions.ClientPage.CreateStudent;
import menu.Actions.ClientPage.Exit_Action;
import menu.Actions.ClientPage.GetStudents;
import menu.Actions.ClientPage.GetStudentById;
import menu.Recivers.WelcomeMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that provides pages for menus in the console.
 */
public class Page {
    private static List<Action> welcome_page;

    /**
     * @param welcomeMenu {@link  menu.Recivers.WelcomeMenu}
     * @return Returns "Page" - a list of home page menu actions.
     */
    public static List<Action> welcomePage(WelcomeMenu welcomeMenu) {
        return welcome_page == null ? createClientPage(welcomeMenu) : welcome_page;
    }

    public static List<Action> clientPage(WelcomeMenu welcomeMenu) {
        return welcome_page == null ? createClientPage(welcomeMenu) : welcome_page;
    }


    private static List<Action> createClientPage(WelcomeMenu welcomeMenu) {
        return new ArrayList<Action>() {{
            add(new Exit_Action(this.size(), welcomeMenu));
            add(new GetStudentById(this.size(), welcomeMenu));
            add(new GetStudents(this.size(), welcomeMenu));
            add(new CreateStudent(this.size(), welcomeMenu));
        }};
    }
}
