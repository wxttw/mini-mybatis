package com.hjj.test;

import com.wxttw.frameworks.configuration.Configuration;
import com.wxttw.frameworks.configuration.XmlMapperBuilder;
import com.wxttw.frameworks.io.Resources;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author jay
 * @date 2024/4/6 8:34
 * @description: TODO
 */
public class TestXmlMapperBuilder {

    @Test
    public void testXmlMapperBuilder() {
        try {
            Configuration configuration = new Configuration();
            InputStream inputStream = Resources.getResourceAsStream("mappers/UserMapper.xml");
            XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(inputStream,configuration);
            xmlMapperBuilder.parse();
            System.out.println(xmlMapperBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
