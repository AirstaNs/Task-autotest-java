package org.menu.Actions.ClientPage;


import org.menu.Actions.Action;
import org.menu.Controller;
import org.menu.Recivers.ShouldBeExit;

public class Exit_Action extends Action {
    private static final String title = "Exit";

    private final ShouldBeExit menu;

    public Exit_Action(int numberItem, ShouldBeExit menu) {
        super(title, numberItem);
        this.menu = menu;
    }


    @Override
    public void execute(Controller controller) {
        menu.exit();
    }
}