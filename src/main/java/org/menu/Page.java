package org.menu;


import org.menu.Actions.Action;
import menu.Actions.ClientPage.*;
import org.menu.Actions.ClientPage.*;
import org.menu.Recivers.ClientMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that provides pages for menus in the console.
 */
public class Page {


    public static List<Action> clientPage(ClientMenu clientMenu) {
        return  createClientPage(clientMenu);
    }


    private static List<Action> createClientPage(ClientMenu clientMenu) {
        return new ArrayList<Action>() {{
            add(new GetStudents(this.size(), clientMenu));
            add(new GetStudentById(this.size(), clientMenu));
            add(new AddStudent(this.size(), clientMenu));
            add(new RemoveStudent(this.size(), clientMenu));
            add(new Exit_Action(this.size(), clientMenu));
        }};
    }
}
