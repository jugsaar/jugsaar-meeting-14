package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Field;

public class Hck001a_StringEreaser {

    public static void main(String[] args) throws Exception{

        //System.setSecurityManager(new SecurityManager());

        System.out.println("hello!");

        Field valueField = String.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set("hello!", "??????".toCharArray());

        System.out.println("hello!");
    }
}
