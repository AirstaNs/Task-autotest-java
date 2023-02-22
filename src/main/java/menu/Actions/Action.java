package menu.Actions;


import menu.Controller;

// COMMAND
public abstract class Action {
    private final String title;
    private final int numberItem;

    public Action(String title, int numberItem) {
        this.title = title;
        this.numberItem = numberItem;
    }

    public abstract void execute(Controller controller);


    public String getTitle() {
        return title;
    }

    public int getNumberItem() {
        return numberItem;
    }

    @Override
    public String toString() {
        return String.format("%d. %s", getNumberItem(), getTitle());
    }
}
