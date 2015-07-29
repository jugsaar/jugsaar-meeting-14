package de.jugsaar.meeting14.reflectionhacks;

public class Ex001b_WhatIsAClass {

    public static void main(String ... args) throws Exception{

        Class<? extends String> clazz1 = Class.forName("java.lang.String").asSubclass(String.class);

        Class<String> clazz2 = String.class;
        Class<? extends String> clazz3 = "Hello".getClass();

        Object o = 1;
        Integer i1 = (Integer)o;
        Integer i2 = Integer.class.cast(o);

    }
}
