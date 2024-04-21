package com.wxttw.frameworks.mybatis.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * @author jay
 * @date 2024/4/6 1:16
 * @description: TODO
 */

@Slf4j
public class ClassUtil {

    public static Class<?> getClazz(String paramsType) {
        if (StringUtils.isBlank(paramsType)) {
            return null;
        }
        try {
            return Class.forName(paramsType);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static <T> Object getPrivateFieldValue(Class<T> parameterTypeClass, String fieldName, Object object) {
        try {
            Field field = parameterTypeClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}

