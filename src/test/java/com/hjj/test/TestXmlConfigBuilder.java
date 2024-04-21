package com.hjj.test;

import com.wxttw.frameworks.configuration.Configuration;
import com.wxttw.frameworks.configuration.XmlConfigBuilder;
import com.wxttw.frameworks.configuration.XmlMapperBuilder;
import com.wxttw.frameworks.io.Resources;
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
