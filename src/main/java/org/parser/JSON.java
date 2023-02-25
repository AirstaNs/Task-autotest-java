package org.parser;


import org.client.Student;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.parser.Utils.*;

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
                String[] values = element.split(Utils.comma+"");
                int id = Integer.parseInt(values[0].split(":")[1]);
                String name = values[1].split(":")[1].replaceAll("\"", "");
                students1.add(new Student(id, name));
            }
        }
        return students1;
    }

    public String toJson(List<Student> students){
        String students1 = this.toArrayJSON(students, "students");
        String s = Utils.wrapBracketsObj(students1);
        return Utils.addIndent(s);
    }

    public <T> String toArrayJSON(List<T> list, String nameList) {
        if (Objects.isNull(list)) return emptyArray;
        if (Objects.isNull(nameList)) return emptyArray;

        String wrapName = Utils.wrapInQuotes(nameList);
        String nameListWrap = wrapName.concat(JsonDelimiterValue);
        StringBuilder builder = new StringBuilder(nameListWrap);
        builder.setLength(nameListWrap.length());
        builder.append(emptyArray);

        int length = 2 + wrapName.length() + JsonDelimiterValue.length();
        for (T student : list) {
            StringBuilder builder1 = new StringBuilder(toObjJSON(student));
            Utils.dividePairAndObj(builder1);
            builder.insert(length, builder1);
            length += builder1.length();
        }
        builder.trimToSize();
        Utils.removeLastComma(builder);
        return builder.toString();
    }

    public <T> String toObjJSON(T obj) {
        if (Objects.isNull(obj)) return emptyObj;
        Field field = null;
        String pair = emptyObj;

        StringBuilder builder = new StringBuilder(emptyObj);
        int length = emptyObj.length() / 2;
        Field[] declaredFields = obj.getClass().getDeclaredFields();

        for (Field declaredField : declaredFields) {
            try {
                String nameField = declaredField.getName();
                field = obj.getClass().getDeclaredField(nameField);
                field.setAccessible(true);
                Object value = field.get(obj);
                pair = normalizationMapToJSON(nameField, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                if (field != null) {
                    field.setAccessible(false);
                }
            }
            StringBuilder pairs = new StringBuilder();
            pairs.append(pair);
            Utils.dividePairAndObj(pairs);
            builder.insert(length, pairs);
            length += pairs.length();
        }
        Utils.removeLastComma(builder);
        Utils.removeLastNewLine(builder);
        return builder.toString();
    }


    private <T> String normalizationMapToJSON(String name, T value) {
        return createPair(name, value);
    }

    private <T> String createPair(String name, T value) {
        String namePair = Utils.wrapInQuotes(name);
        String valPair = !(value instanceof Number) ? Utils.wrapInQuotes(String.valueOf(value)) : value.toString();
        return String.format("%s%s%s", namePair, JsonDelimiterValue, valPair);
    }
}
