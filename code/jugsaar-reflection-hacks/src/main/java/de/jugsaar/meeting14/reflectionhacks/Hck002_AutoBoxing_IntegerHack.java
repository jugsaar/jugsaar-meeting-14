package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Field;

public class Hck002_AutoBoxing_IntegerHack {

    public static void main(String[] args) throws Exception{

        System.out.println((Integer)42);

        hack();

        System.out.println((Integer)42);

        int sum = ((Integer)42) + 1;
        System.out.println(sum);

        unhack();

        System.out.println((Integer) 42);
    }

    static void hack() throws Exception {

        Field valueField = Integer.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(42, 22);
    }

    static void unhack() throws Exception{

        Field valueField = Integer.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(42, new Integer(42));
    }
}
