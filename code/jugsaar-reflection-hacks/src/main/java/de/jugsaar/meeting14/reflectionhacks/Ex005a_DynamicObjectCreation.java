package de.jugsaar.meeting14.reflectionhacks;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Ex005a_DynamicObjectCreation {

    public static class Foo extends FileInputStream {

        public Foo() throws FileNotFoundException {
            super("abc.xml");
        }
    }

    public static void main(String[] args) {

        try {
            InputStream is = Foo.class.newInstance();
            System.out.println("ok");
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("oops - " + e);
        } catch (RuntimeException | Error e) {
            System.out.println("Not this - " + e);
        } catch (Throwable t) {
            System.out.println("Something went seriously wrong - " + t);

            try {
                sanitize(t);
            } catch (FileNotFoundException fnfe) {
                System.out.println(fnfe.getMessage());
            } catch(Throwable t2){
                System.out.println("Nope can't deal with it");
            }
        }
    }

    private static void sanitize(Throwable t) throws Throwable{
        throw t;
    }
}
