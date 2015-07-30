package de.jugsaar.meeting14.reflectionhacks;

import javax.accessibility.Accessible;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * Created by tom on 29.07.15.
 */
public class Ex004_ReadWriteObjectState {

    static class Person {

        private static CharSequence PREFIX = "Person>";

        private final String name;

        public Person(String name) {
            this.name = name;
        }

        public String toString() {
            return PREFIX + "{" + "name='" + name + '\'' + '}';
        }
    }

    public static void main(String[] args) throws Exception {

        Person person = new Person("Fritz Katz");
        System.out.println(person);

        Field prefixField = person.getClass().getDeclaredField("PREFIX");
        prefixField.setAccessible(true);
        prefixField.set(null, "Räuber>");

        Field nameField = person.getClass().getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(person, "Hotzenplotz");

        System.out.println(person);
    }
}
