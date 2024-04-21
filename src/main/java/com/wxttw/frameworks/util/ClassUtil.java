package com.wxttw.frameworks.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * @author jay
 * @date 2024/4/6 1:16
 * @description: TODO
 */

public class ClassUtil {

    public static Class<?> getClazz(String paramsType) {
        if (StringUtils.isBlank(paramsType)) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(paramsType);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> Object getPrivateFieldValue(Class<T> parameterTypeClass, String fieldName, Object object) {
        try {
            Field field = parameterTypeClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}

