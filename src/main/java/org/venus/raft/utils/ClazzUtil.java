package org.venus.raft.utils;

import java.lang.reflect.Field;

public class ClazzUtil {

    public static <T> void setObjectField(T obj,
                                          String fieldName,
                                          Class<?> classType,
                                          Object value) {
        try {
            Field field = classType.getDeclaredField(fieldName);
            boolean fieldAccessible = field.isAccessible();
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(fieldAccessible);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
