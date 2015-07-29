package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.ParameterizedType;

/**
 * Created by tom on 29.07.15.
 */
public class GenericTypeParameterReflection {

    public static void main(String[] args) {

        Container<Integer> container = new Container<Integer>() {
            @Override
            public Integer get(int index) {
                return null;
            }
        };

        ParameterizedType paramType = (ParameterizedType)container.getClass().getGenericSuperclass();

        System.out.println(paramType);
    }

    interface Container<T>{

        T get(int index);

    }
}
