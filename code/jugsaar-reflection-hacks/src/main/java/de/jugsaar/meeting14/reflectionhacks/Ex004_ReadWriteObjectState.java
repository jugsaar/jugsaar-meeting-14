package de.jugsaar.meeting14.reflectionhacks;

import javax.accessibility.Accessible;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * Created by tom on 29.07.15.
 */
public class Ex004_ReadWriteObjectState {

    static class Person {

        private final String firstname;
        private final String lastname;

        public Person(String firstname, String lastname) {
            this.firstname = firstname;
            this.lastname = lastname;
        }

        public String toString() {
            return "Person{" + "firstname='" + firstname + '\'' + ", lastname='" + lastname + '\'' + '}';
        }
    }

    public static void main(String[] args) throws Exception {

        Person person = new Person("Fritz", "Katz");
        System.out.println(person);

        Field firstnameField = person.getClass().getDeclaredField("firstname");
        firstnameField.setAccessible(true);
        firstnameField.set(person, "Foo");

        Field lastnameField = person.getClass().getDeclaredField("lastname");
        lastnameField.setAccessible(true);
        lastnameField.set(person, "Foo");
        System.out.println(person);
    }
}
