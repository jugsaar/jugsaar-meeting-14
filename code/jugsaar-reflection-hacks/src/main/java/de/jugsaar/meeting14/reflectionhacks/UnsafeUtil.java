package de.jugsaar.meeting14.reflectionhacks;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by tom on 27.07.15.
 */
public class UnsafeUtil {

    static final Unsafe unsafe;

    static {
        try {
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);

            unsafe = (Unsafe) theUnsafeField.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't access unsafe", e);
        }
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }
}
