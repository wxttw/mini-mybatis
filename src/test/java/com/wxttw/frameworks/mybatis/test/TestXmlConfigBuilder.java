package com.wxttw.frameworks.mybatis.test;

import com.wxttw.frameworks.mybatis.configuration.XmlConfigBuilder;
import com.wxttw.frameworks.mybatis.io.Resources;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author jay
 * @date 2024/4/6 8:34
 * @description: TODO
 */
public class TestXmlConfigBuilder {

    @Test
    public void testXmlConfigBuilder() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(inputStream);
            xmlConfigBuilder.parse();
            System.out.println(xmlConfigBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
