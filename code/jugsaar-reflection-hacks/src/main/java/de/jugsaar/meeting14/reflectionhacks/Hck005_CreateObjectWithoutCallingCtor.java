package de.jugsaar.meeting14.reflectionhacks;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Hck005_CreateObjectWithoutCallingCtor {

    static class Resource {

        private DbConnection connection;

        public Resource() {
            connection = connectToDatabase();
        }

        private DbConnection connectToDatabase() {
            throw new RuntimeException("database not available");
        }
    }

    interface DbConnection {
    }

    public static void main(String[] args) {

        System.err.println("# via new ()");
        try {
            new Resource();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.err.println();


        System.err.println("# via class.newInstance");
        try {
            Resource.class.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.err.println();


        System.err.println("# by-passing the ctor via custom constructor with ReflectionFactory");
        try {
            Constructor<?> ctor = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(Resource.class, Object.class.getConstructor());
            Resource res = (Resource) ctor.newInstance();
            System.err.println(res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.err.println();


        System.err.println("# by-passing the ctor via Unsafe allocateInstance");
        try {
            Resource res = (Resource) UnsafeUtil.getUnsafe().allocateInstance(Resource.class);
            System.err.println(res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.err.println();
    }
}