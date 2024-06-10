package com.wxttw.frameworks.mybatis.util;

import com.wxttw.frameworks.mybatis.logging.Log;
import com.wxttw.frameworks.mybatis.logging.LogFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jay
 * @date 2024/4/6 1:16
 * @description: TODO
 */


public class ClassUtil {

    private static final Log log = LogFactory.getLog(ClassUtil.class);

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

    public static <T> Object getPrivateFieldValue(Class<T> parameterTypeClass, Object parameter, String fieldName) {
        try {
            Field field = parameterTypeClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(parameter);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static Class<?> getFieldType(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return field.getType();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static Set<Class<?>> scanClasses(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File directory = new File(classLoader.getResource(path).getFile());
        if (directory.exists()) {
            scanDirectory(directory, packageName, classes);
        }
        return classes;
    }

    private static void scanDirectory(File directory, String packageName, Set<Class<?>> classes) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file, packageName + "." + file.getName(), classes);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

