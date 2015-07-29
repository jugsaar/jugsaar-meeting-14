package de.jugsaar.meeting14.reflectionhacks.agent;

/**
 * Created by tom on 27.07.15.
 */
public class ProfileUtil {

    public static void start(String className, String methodName) {
        System.out.println(className + '\t' + methodName + "\tstart\t" + System.currentTimeMillis());
    }

    public static void end(String className, String methodName) {
        System.out.println(className + '\t' + methodName + "\tend\t" + System.currentTimeMillis());
    }
}
