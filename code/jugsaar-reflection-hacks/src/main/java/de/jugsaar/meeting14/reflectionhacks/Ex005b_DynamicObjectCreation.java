package de.jugsaar.meeting14.reflectionhacks;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Ex005b_DynamicObjectCreation {


    public static class Foo extends FileInputStream {

        public Foo() throws FileNotFoundException {
            super("abc.xml");
        }
    }

    public static void main(String[] args) {

        try {
            Constructor<? extends InputStream> ctr = Foo.class.getConstructor();
            InputStream is = ctr.newInstance();
            System.out.println("ok");
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("oops - " + e); //should not happen
        } catch (NoSuchMethodException e) {
            System.out.println("Ctor doesn't exist - " + e); //cannot happen
        } catch (InvocationTargetException e) {
            System.out.println(e.getCause()); //we end here
        }
    }

    private static void sanitize(Throwable t) throws Throwable{
        throw t;
    }
}
