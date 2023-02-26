package org.client;


/**
 * Class describing a student
 */
public class Student implements Comparable<Student> {

   private int id;
 private    String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName() + " " + getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (this.hashCode() != o.hashCode()) return false;

        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        if (this.getId() != student.getId()) return false;
        return this.getName().equals(student.getName());
    }

    @Override
    public int hashCode() {
        int result = this.getId();
        result = 31 * result + this.getName().hashCode();
        return result;
    }

    @Override
    public int compareTo(Student o) {
        return this.getName().compareTo(o.getName());
    }
}
