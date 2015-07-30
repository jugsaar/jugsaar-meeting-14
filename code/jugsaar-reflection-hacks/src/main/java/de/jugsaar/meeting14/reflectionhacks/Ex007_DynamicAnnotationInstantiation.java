package de.jugsaar.meeting14.reflectionhacks;

import sun.reflect.annotation.AnnotationParser;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 17.06.15.
 */
public class Ex007_DynamicAnnotationInstantiation {

    @Example("required")
    static class X {
    }

    @Example("default")
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Example {
        String value();

        int foo() default 42;

        boolean bar() default true;
    }

    public static void main(String[] args) {

        System.out.println(X.class.isAnnotationPresent(Example.class));

        System.out.printf("Traditional annotation creation: %s%n",
                X.class.getAnnotation(Example.class));
        System.out.printf("Traditional annotation creation: %s%n",
                Example.class.getAnnotation(Example.class));

        System.out.printf("Custom annotation creation: %s%n",
                createAnnotationInstance(Collections.singletonMap("value", "required"), Example.class));
    }

    private static <A extends Annotation> A createAnnotationInstance(Map<String, Object> customValues, Class<A> annotationType) {

        Map<String, Object> values = new HashMap<>();

        //Extract default values from annotation
        for (Method method : annotationType.getDeclaredMethods()) {
            values.put(method.getName(), method.getDefaultValue());
        }

        //Populate required values
        values.putAll(customValues);

        return (A) AnnotationParser.annotationForMap(annotationType, values);
    }

}
