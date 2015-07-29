package de.jugsaar.meeting14.reflectionhacks;

import sun.reflect.Reflection;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

public class UC001_DetectCallingClassExamples {

    static class Application {

        //Logger logger = Logger.getLogger(Application.class.getName());
        Logger logger = LoggerFactory.create();

        public void run() {

            logger.info("Application started");
        }
    }

    public static void main(String[] args) {

        new Application().run();

    }

    static class LoggerFactory {

        public static Logger create() {
            return Logger.getLogger(getCallerClassName());
        }

        private static String getCallerClassName() {

            String name = getCallerClass().getName();
            System.out.println("got caller class name: " + name);

            return name;
        }

        private static Class<?> getCallerClass() {

            return null;

            //via Exception Stacktrace inspection
//            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
//            try {
//                return Class.forName(stackTrace[3].getClassName(), true, LoggerFactory.class.getClassLoader());
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }

            //via Exception Stacktrace inspection from Thread
//            try {
//			return Class.forName(Thread.currentThread().getStackTrace()[4].getClassName());
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }

            // via sun.misc.Reflection
//            return Reflection.getCallerClass(4);

            //via custom SecurityManager
//          return new CustomSecurityManager().getCallerClasses()[4];

            //MethodHandles and lookupClass() - doesn't provide deeper look in the stack - returns just the "direct" caller
//            return MethodHandles.lookup().lookupClass();
        }
    }

    static class CustomSecurityManager extends SecurityManager {

        public Class[] getCallerClasses() {
            return super.getClassContext();
        }

    }
}
