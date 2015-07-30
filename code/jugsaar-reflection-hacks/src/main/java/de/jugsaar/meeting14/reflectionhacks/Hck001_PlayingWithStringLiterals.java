package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Field;

public class Hck001_PlayingWithStringLiterals {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {


        System.out.println("Hello world");

        hack("Hello world");

        System.out.println("Hello world");

    }


































    private static void hack(String string) throws NoSuchFieldException, IllegalAccessException {

        Field valueField = String.class.getDeclaredField("value");
        valueField.setAccessible(true);
        char[] value = (char[]) valueField.get(string);


        char[] clonedValue = value.clone();
        for (int i = 0; i < value.length; i++) {
            value[i] = clonedValue[value.length - 1 - i];
        }
    }
}
