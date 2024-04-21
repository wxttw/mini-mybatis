package com.wxttw.frameworks.session;

import com.wxttw.frameworks.configuration.Configuration;
import com.wxttw.frameworks.configuration.XmlConfigBuilder;
import com.wxttw.frameworks.session.defaults.DefaultSqlSessionFactory;

import java.io.InputStream;

/**
 * @author jay
 * @date 2024/4/7 15:36
 * @description: TODO
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream inputStream) {
        XmlConfigBuilder parser = new XmlConfigBuilder(inputStream);
        return build(parser.parse());
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }

}
