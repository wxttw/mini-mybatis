package com.wxttw.frameworks.io;

import java.io.InputStream;

/**
 * @author jay
 * @date 2024/4/5 23:35
 * @description: TODO
 */
public class Resources {

    public static InputStream getResourceAsStream(String path)
    {
        ClassLoader classLoader = Resources.class.getClassLoader();
        return classLoader.getResourceAsStream(path);
    }
}
