package com.example.liuwangshu.pluginservice.HookLibrary;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/3/24 0024.
 */

public class FieldUtil {
    public static Object getField(Class clazz, Object target, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }
    public static Field getField(Class clazz, String name) throws Exception{
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }
    public static void setField(Class clazz, Object target, String name, Object value) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
