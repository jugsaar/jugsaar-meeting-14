package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Horrible benchmark... use JMH for real experiments
 *
 * Run with -Dsun.reflect.noCaches=true
 */
public class Ex001d_CostOfReflection {

    public static void main(String[] args) throws Exception {

        long time = System.currentTimeMillis();

        for (int i = 0; i < 1e6; i++) {
            Field field = String.class.getDeclaredField("value");
            Method method = String.class.getDeclaredMethod("toString");
        }

        time = System.currentTimeMillis() - time;
        System.out.printf("time = %sms", time);
    }
}
