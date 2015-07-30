package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by tom on 29.07.15.
 */
public class GenericsReflection {

    public static void main(String[] args) throws Exception {

//        Container<Integer> container = new Container<Integer>() {
//            @Override
//            public Integer get(int index) {
//                return null;
//            }
//        };
//
//        ParameterizedType paramType = (ParameterizedType)container.getClass().getGenericSuperclass();
//
//        System.out.println(paramType);

        Field containersField = Service.class.getDeclaredField("containers");
        ParameterizedType parameterizedType = (ParameterizedType) containersField.getGenericType();

        System.out.println(parameterizedType);
        System.out.printf("Raw Type: %s%n", parameterizedType.getRawType());
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        System.out.printf("key: %s%n", actualTypeArguments[0]);
        System.out.printf("Value: %s%n", actualTypeArguments[1]);
    }

    interface Container<T> {

        T get(int index);

    }

    static class Service {
        Map<String, Container<String>> containers;
    }
}
