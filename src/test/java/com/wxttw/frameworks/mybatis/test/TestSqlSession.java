package com.wxttw.frameworks.mybatis.test;

import com.hjj.test.mapper.UserMapper;
import com.hjj.test.pojo.User;
import com.wxttw.frameworks.mybatis.io.Resources;
import com.wxttw.frameworks.mybatis.session.SqlSession;
import com.wxttw.frameworks.mybatis.session.SqlSessionFactory;
import com.wxttw.frameworks.mybatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/6 8:34
 * @description: TODO
 */
public class TestSqlSession {

    @Test
    public void testSelectOne() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = factory.openSession();

            User user = sqlSession.selectOne("com.hjj.test.mapper.UserMapper.selectOne", 1L);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectList() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = factory.openSession();

            List<User> userList = sqlSession.selectList("com.hjj.test.mapper.UserMapper.selectList", null);
            System.out.println(userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = factory.openSession();

            User user = new User();
            user.setUsername("henry");
            Integer res = sqlSession.insert("com.hjj.test.mapper.UserMapper.insert", user);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdate() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = factory.openSession();

            User user = new User();
            user.setId(3L);
            user.setUsername("leo");
            Integer res = sqlSession.insert("com.hjj.test.mapper.UserMapper.update", user);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = factory.openSession();

            Integer res = sqlSession.delete("com.hjj.test.mapper.UserMapper.delete", 3L);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testMapper() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = factory.openSession();

            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> list = mapper.selectList();
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateWithCommitAndRollback1() {
        SqlSession sqlSession = null;
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            sqlSession = factory.openSession(false);

            User user = new User();
            user.setId(1L);
            user.setUsername("leo4");
            Integer res = sqlSession.insert("com.hjj.test.mapper.UserMapper.update", user);
            sqlSession.commit();
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateWithCommitAndRollback2() {
        SqlSession sqlSession = null;
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            sqlSession = factory.openSession(false);

            User user = new User();
            user.setId(1L);
            user.setUsername("leo2");
            Integer res = sqlSession.insert("com.hjj.test.mapper.UserMapper.update", user);
            int i = 1/0;
            sqlSession.commit();
            System.out.println(res);
        } catch (Exception e) {
            if (sqlSession != null) {
                sqlSession.rollback();
            }
            e.printStackTrace();
        }
    }
}
