package org.parser;


import org.client.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JSON {

    /*
    Converting a method from json to a list of students.
     */
    public List<Student> fromJson(String json) {

        //The structure to store the JSON
        int startIndex = json.indexOf(Utils.openSquareBracket) + 1;
        int endIndex = json.lastIndexOf(Utils.closeSquareBracket);
        String studentListStr = json.substring(startIndex, endIndex).replaceAll("(\\s+|\\{)", "");
        String[] jsonElements = studentListStr.split("\\}+\\,*"); // \{+.*\}+,*

        List<Student> students1 = new ArrayList<>();
        //Loop through each element
        for (String element : jsonElements) {
            //If the element is not empty
            if (!element.isEmpty()) {
                //Split the element into key-value pairs
                String[] values = element.split(Utils.comma);
                int id = Integer.parseInt(values[0].split(":")[1]);
                String name = values[1].split(":")[1].replaceAll("\"", "");
                students1.add(new Student(id, name));
            }
        }
        return students1;
    }
}
