package menu.Recivers;


import menu.Controller;
import org.model.Student;
import org.parser.JSON;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



public class ClientMenu implements ShouldBeExit {

    public Optional<Student> getStudentById(Controller controller, int id) {
        Optional<Student> optStudent = Optional.empty();
        try {
            optStudent = getListStudents(controller).stream().filter(student -> student.getId() == id).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optStudent;
    }

    public void addStudent(String name, Controller controller) {
        try {
            List<Student> listStudents = getListStudents(controller);
            int other = 1;
            int maxID = listStudents.stream().mapToInt(Student::getId).max().orElse(other);
            listStudents.add(new Student(maxID,name));
            uploadFile(controller,listStudents);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> getStudents(Controller controller, String name) {
        List<Student> students = Collections.emptyList();
        try {
            students = getListStudents(controller)
                    .stream()
                    .filter(student -> student.getName().equals(name))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public void removeStudent(Controller controller, int id) throws Exception {
        List<Student> listStudents = getListStudents(controller);
        boolean b = listStudents.removeIf(student -> student.getId() == id);
        if (b) uploadFile(controller, listStudents);
    }

    private static void uploadFile(Controller controller, List<Student> listStudents) throws Exception {
        JSON json = new JSON();
        String jsonStr = json.toJson(listStudents);
        controller.getFtpClient().replaceFile(controller.getFileName(), jsonStr);
    }

    private List<Student> getListStudents(Controller controller) throws Exception {
        String file = controller.getFtpClient().getFile(controller.getFileName());
        JSON json = new JSON();
        return json.fromJson(file);
    }
}