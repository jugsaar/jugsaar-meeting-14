package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Field;

public class Hck001_PlayingWithStringLiterals {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        Field valueField = String.class.getDeclaredField("value");
        valueField.setAccessible(true);

        String string = "Hello world";
        char[] value = (char[]) valueField.get(string);
        System.out.println(string);

        char[] clonedValue = value.clone();
        for (int i = 0; i < value.length; i++) {
            value[i] = clonedValue[value.length - 1 - i];
        }
        System.out.println(string);

    }
}
