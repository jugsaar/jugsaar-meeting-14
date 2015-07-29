package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by tom on 29.07.15.
 *
 *  Goto
 * /home/tom/dev/repos/gh/jugsaar/jugsaar-meeting-14/code/jugsaar-reflection-hacks/src/main/java
 *
 *  javac de/jugsaar/meeting14/reflectionhacks/Uc005_ParameterNameReflectionExample.java
 *  java -cp . de.jugsaar.meeting14.reflectionhacks.Uc005_ParameterNameReflectionExample
 *
 *  javac -parameters de/jugsaar/meeting14/reflectionhacks/Uc005_ParameterNameReflectionExample.java
 *  java -cp . de.jugsaar.meeting14.reflectionhacks.Uc005_ParameterNameReflectionExample
 */
public class Uc005_ParameterNameReflectionExample {

    public static void main(String[] args) throws Exception {

        Method method = Uc005_ParameterNameReflectionExample.class.getDeclaredMethod("method", int.class, String.class);
        System.out.println(method.getName());
        for (Parameter parameter : method.getParameters()) {
            System.out.println("Param: " + parameter.getName() + " " + parameter.getType());
        }
    }

    public static void method(int intParam, String stringParam) {
        System.out.println("x");
    }
}
