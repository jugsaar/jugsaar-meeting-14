package de.jugsaar.meeting14.reflectionhacks;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by tom on 29.07.15.
 */
public class Uc004_CallDefaultMethodViaReflection {

    interface Hello {

        default String hello() {
            return "Hello";
        }
    }

    public static void main(String[] args) throws Throwable {

        Hello target =
//                new Hello() {};
        (Hello) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{Hello.class}, (Object proxy, Method method, Object[] arguments) -> null);

        Method method = Hello.class.getMethod("hello");

        System.out.println(method.invoke(target));

























//
//
//
//
//
//
        Object result = MethodHandles.lookup()
                .in(method.getDeclaringClass())
                .unreflectSpecial(method, method.getDeclaringClass())
                .bindTo(target)
                .invokeWithArguments();

        System.out.println(result); //Hello
    }

}
