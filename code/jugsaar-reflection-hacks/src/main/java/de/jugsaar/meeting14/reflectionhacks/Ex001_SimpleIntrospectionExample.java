package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Ex001_SimpleIntrospectionExample {


    static class Base {

        private String field0;
        protected int field1;

        public String someMethod() {
            return null;
        }
    }

    static class Derived extends Base {

        private double field2;

        public Derived(double field2){
            this.field2 = field2;
        }

        public static void someStaticMethod() {
        }
    }


    public static void main(String[] args) throws Exception {

        Base o = new Derived(0xDEADC0DE);

        ObjectInspector.inspect(o);
    }

    static class ObjectInspector {

        public static void inspect(Object o) throws Exception {
            log("Inspecting object instance: %s%n", o);
            inspect(o.getClass());
        }

        private static void inspect(Class<? extends Object> clazz) throws Exception {

            log("%nInspecting class: %s%n", clazz);

            log("%nConstructor:%n");
            for (Constructor ctor : clazz.getDeclaredConstructors()) {
                log("declared constructor: %s%n", ctor);
            }

            log("%nFields:%n");
            for (Field field : clazz.getDeclaredFields()) {
                log("declared field: %s%n", field);
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