package com.wxttw.frameworks.session.defaults;

import com.wxttw.frameworks.configuration.Configuration;
import com.wxttw.frameworks.session.SqlSession;
import com.wxttw.frameworks.session.SqlSessionFactory;

/**
 * @author jay
 * @date 2024/4/7 15:33
 * @description: TODO
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration, null);
    }
}
