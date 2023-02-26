package org.menu.Actions.ClientPage;

import org.menu.Actions.Action;
import org.menu.Controller;
import org.menu.Recivers.ClientMenu;
import org.model.Student;

import java.util.List;

public class GetAllStudents extends Action {
    private static final String title = "Get all students";
    private final ClientMenu clientMenu;

    public GetAllStudents(int numberItem, ClientMenu clientMenu) {
        super(title, numberItem);
        this.clientMenu = clientMenu;
    }


    @Override
    public void execute(Controller controller) {
        try {
            List<Student> studentList = clientMenu.getListStudents(controller);
            if (studentList.isEmpty()) {
                System.out.println("Student not found");
            } else {
                this.printStudentsToConsole(studentList);
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    private void printStudentsToConsole(List<Student> students) {
        System.out.println("Students list:");
        students.stream().sorted().forEach(student -> System.out.println(">> " + student));
        System.out.println("End of list");
    }

}