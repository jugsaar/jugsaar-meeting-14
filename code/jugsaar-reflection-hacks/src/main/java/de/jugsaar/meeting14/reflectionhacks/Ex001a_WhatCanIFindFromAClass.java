package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class Ex001a_WhatCanIFindFromAClass {

    public static void main(String[] args) {

        Class clazz = ArrayList.class;

        System.out.println("clazz.getSuperclass() = " + clazz.getSuperclass());

        System.out.println(Modifier.isFinal(clazz.getModifiers()));
        System.out.println(Modifier.isFinal(String.class.getModifiers()));
    }
}
