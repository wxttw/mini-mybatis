package com.wxttw.frameworks.mybatis.test;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.configuration.XmlMapperBuilder;
import com.wxttw.frameworks.mybatis.io.Resources;
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
