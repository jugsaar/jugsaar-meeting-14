package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Ex002_DeclaredVsDefaultInReflection {

    static class Base{

        private void method1(){}
        protected void method2(){}
        public void method3(){}
    }

    static class Derived extends Base{

        private void method4(){}
        protected void method5(){}
        public void method6(){}
    }

    public static void main(String... args) {

        System.out.println("Showing methods:");
        Method[] methods = Derived.class.getMethods();

        System.out.printf("Found %s Methods%n", methods.length);
        for (Method m : methods) {
            System.out.println("\t" + m);
        }


        Method[] declaredMethods = Derived.class.getDeclaredMethods();
        System.out.printf("Found %s Declared Methods%n",declaredMethods.length);
        for (Method m : declaredMethods) {
            System.out.println("\t" + m);
        }
    }
}
