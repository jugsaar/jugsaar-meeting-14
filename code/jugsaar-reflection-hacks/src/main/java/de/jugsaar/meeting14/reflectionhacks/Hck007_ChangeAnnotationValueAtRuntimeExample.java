package de.jugsaar.meeting14.reflectionhacks;

import static java.util.Collections.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;

public class Hck007_ChangeAnnotationValueAtRuntimeExample {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Example {
        String value();
    }

    @Example("Test")
    static class Foo {
    }

    public static void main(String[] args) {

        Foo foo = new Foo();

        Example exampleAnnotation = foo.getClass().getAnnotation(Example.class);
        System.out.println(exampleAnnotation);

        changeValuesOfAnnotation(exampleAnnotation, singletonMap("value", "JUGSaar"));

        System.out.println(foo.getClass().getAnnotation(Example.class));
    }

    private static void changeValuesOfAnnotation(Object annotation, Map<String, String> newValues) {

        if (!Proxy.isProxyClass(annotation.getClass())) {
            return;
        }

        Object invocationHandler = Proxy.getInvocationHandler(annotation);
        try {
            Field field = invocationHandler.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            field.set(invocationHandler, newValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}