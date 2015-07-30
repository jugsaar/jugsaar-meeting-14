package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Ex001_IntrospectionExample {

    static class Obj {

        private String field0 = "hallo";
        protected int field1 = 42;

        public String someMethod() {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {

        Obj o = new Obj();

        ObjectInspector.inspect(o);
    }













    static class ObjectInspector {

        public static void inspect(Object o) throws Exception {

            log("Inspecting object instance: %s%n", o);
            Class<?> clazz = o.getClass();

            log("%nInspecting class: %s%n", clazz);

            log("%nConstructor:%n");
            for (Constructor ctor : clazz.getDeclaredConstructors()) {
                log("declared constructor: %s%n", ctor);
            }

            log("%nFields:%n");
            for (Field field : clazz.getDeclaredFields()) {

                field.setAccessible(true);
                log("declared field: %s with value: %s%n", field, field.get(o));
            }

            log("%nMethods%n");
            for (Method method : clazz.getDeclaredMethods()) {
                log("declared methods: %s%n", method);
            }

            if (clazz.getSuperclass() != Object.class) {
                inspect(clazz.getSuperclass());
            }
        }

        private static void log(String msg, Object... args) {
            System.out.printf(msg, args);
        }

    }
}