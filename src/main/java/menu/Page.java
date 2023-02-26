package menu;


import menu.Actions.Action;
import menu.Actions.ClientPage.AddStudent;
import menu.Actions.ClientPage.Exit_Action;
import menu.Actions.ClientPage.GetStudents;
import menu.Actions.ClientPage.GetStudentById;
import menu.Recivers.ClientMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that provides pages for menus in the console.
 */
public class Page {
    private static List<Action> welcome_page;

    private static List<Action> client_Page;

    /**
     * @param clientMenu {@link  ClientMenu}
     * @return Returns "Page" - a list of home page menu actions.
     */
    public static List<Action> welcomePage(ClientMenu clientMenu) {
        return welcome_page == null ? createClientPage(clientMenu) : welcome_page;
    }

    public static List<Action> clientPage(ClientMenu clientMenu) {
        return welcome_page == null ? createClientPage(clientMenu) : welcome_page;
    }


    private static List<Action> createClientPage(ClientMenu clientMenu) {
        return new ArrayList<Action>() {{
            add(new Exit_Action(this.size(), clientMenu));
            add(new GetStudentById(this.size(), clientMenu));
            add(new GetStudents(this.size(), clientMenu));
            add(new AddStudent(this.size(), clientMenu));
        }};
    }
}
