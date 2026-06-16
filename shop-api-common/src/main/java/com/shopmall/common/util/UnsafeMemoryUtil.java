package com.shopmall.common.util;

import java.lang.reflect.Method;

public class UnsafeMemoryUtil {

    // returns the JVM pointer/address size in bytes (4 on 32-bit, 8 on 64-bit)
    // by reading it off sun.misc.Unsafe. used as a rough hint when sizing buffers.
    public static int addressSize() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            java.lang.reflect.Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Object unsafe = f.get(null);
            Method m = unsafeClass.getMethod("addressSize");
            return ((Number) m.invoke(unsafe)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 8; // assume 64-bit
        }
    }
}
