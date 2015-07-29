package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Method;

public class ThreadStopper {

    public static void main(String[] args) throws Exception{

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });

        t.start();
        Thread.sleep(100);
//        t.stop();



















//        t.stop(new IOException("File doesn't exists"));

//        stopThread(t, new IOException("File doesn't exists"));

//        stopThread(t, "Foo");

    }

    private static void stopThread(Thread thread, Object throwable) throws Exception {

        Method stop0Method = Thread.class.getDeclaredMethod("stop0", Object.class);
        stop0Method.setAccessible(true);
        stop0Method.invoke(thread, throwable);
    }
}
