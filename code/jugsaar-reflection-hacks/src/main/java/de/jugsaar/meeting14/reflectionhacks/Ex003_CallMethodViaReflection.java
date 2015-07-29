package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public class Ex003_CallMethodViaReflection {

    public static void main(String[] args) throws Exception{

        Collection<String> names = new ArrayList<>();

        Method addMethod = Collection.class.getMethod("add", Object.class);

        addMethod.invoke(names, "Foo");
        addMethod.invoke(names, "Bar");

        Method toStringMethod = Object.class.getMethod("toString");

        System.out.println(toStringMethod.invoke(names));
    }
}
