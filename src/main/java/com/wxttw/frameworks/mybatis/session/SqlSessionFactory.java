package com.wxttw.frameworks.mybatis.session;

/**
 * @author jay
 * @date 2024/4/7 14:14
 * @description: TODO
 */
public interface SqlSessionFactory {

    SqlSession openSession();

    SqlSession openSession(boolean autoCommit);
}
