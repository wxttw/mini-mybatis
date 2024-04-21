package com.wxttw.frameworks.mybatis.util;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @author jay
 * @date 2024/4/13 22:07
 * @description: TODO
 */
public class DocumentReader {

    public static Document createDocument(InputStream inputStream) {
        try {
            SAXReader saxReader = new SAXReader();
            return saxReader.read(inputStream);
        }catch (Exception e) {
            throw new RuntimeException("Error creating document instance.  Cause: " + e, e);
        }
    }
}
