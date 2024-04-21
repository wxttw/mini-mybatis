package com.wxttw.frameworks.session.defaults;

import com.wxttw.frameworks.configuration.Configuration;
import com.wxttw.frameworks.executor.Executor;
import com.wxttw.frameworks.executor.SimpleExecutor;
import com.wxttw.frameworks.mapping.MappedStatement;
import com.wxttw.frameworks.session.SqlSession;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author jay
 * @date 2024/4/7 14:34
 * @description: TODO
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;

        if (Objects.isNull(executor)) {
            this.executor = new SimpleExecutor();
        } else {
            //TODO:
        }
    }

    @Override
    public <T> T selectOne(String statementId, Object params) throws SQLException {

        List<T> list = this.selectList(statementId, params);

        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException(
                    "通过selectOne()方法，期待返回一个结果, 但实际返回结果数为: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public <T> List<T> selectList(String statementId, Object params) throws SQLException {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.selectList(configuration, mappedStatement, params);
    }

/*    @Override
    public <T> T getMapper(Class<?> classType) {
        Proxy.newProxyInstance(classType.getClassLoader(), new Class[]{classType}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {

                String methodName = method.getName();

                return null;
            }
        });
        return null;
    }*/
}
