package de.jugsaar.meeting14.reflectionhacks;

import java.io.IOException;

/**
 * Created by tom on 29.07.15.
 */
public class Hck003_ThrowUncheckedException {

    public static void main(String[] args) {

        //throw new RuntimeException("");
//        throw new IOException("");

//        Thrower.throwException(new IOException("something went wrong!"));
        throwViaGenerics(new IOException("something went wrong!"));

    }

    static class Thrower{

        private static Throwable toThrow;

        public Thrower() throws Throwable{
            throw toThrow;
        }

        public static void throwException(Throwable throwable){

            toThrow = throwable;

            try {
                Thrower.class.newInstance(); //Java API loop-hole -> not all exceptions are converted to IE, IAE
            } catch (InstantiationException e) {
                e.printStackTrace(); //cannot happen
            } catch (IllegalAccessException e) {
                e.printStackTrace(); //cannot happen
            }
        }
    }


































    //


    public static <T extends Throwable> void throwViaGenerics(Throwable t) throws T{
        throw (T)t;
    }
}
