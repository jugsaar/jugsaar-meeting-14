package de.jugsaar.meeting14.reflectionhacks;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Ex005_DynamicObjectCreation {

    public static void main(String[] args) throws Exception {

        System.out.printf("Via new instance: %s%n", ArrayList.class.newInstance());

        System.out.printf("Via constructor: %s%n", ArrayList.class.getConstructor(Collection.class).newInstance(Arrays.asList(1, 2, 3, 4)));
    }
}
