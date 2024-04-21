package com.hjj.test;

import com.wxttw.frameworks.configuration.XmlConfigBuilder;
import com.wxttw.frameworks.io.Resources;
import com.wxttw.frameworks.session.SqlSession;
import com.wxttw.frameworks.session.SqlSessionFactory;
import com.wxttw.frameworks.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author jay
 * @date 2024/4/6 8:34
 * @description: TODO
 */
public class TestSqlSessionFactoryBuilder {

    @Test
    public void testXmlConfigBuilder() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = factory.openSession();
            System.out.println(sqlSession);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
