package com.hjj.test;

import com.hjj.test.pojo.User;
import com.wxttw.frameworks.io.Resources;
import com.wxttw.frameworks.session.SqlSession;
import com.wxttw.frameworks.session.SqlSessionFactory;
import com.wxttw.frameworks.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/6 8:34
 * @description: TODO
 */
public class TestSqlQuery {

    @Test
    public void testSqlQuery() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = factory.openSession();

            User user = sqlSession.selectOne("basic.selectList", 1L);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
