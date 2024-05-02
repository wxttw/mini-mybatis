package com.wxttw.frameworks.mybatis.session.defaults;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.executor.Executor;
import com.wxttw.frameworks.mybatis.executor.SimpleExecutor;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import com.wxttw.frameworks.mybatis.session.SqlSession;
import com.wxttw.frameworks.mybatis.util.SqlCommandType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author jay
 * @date 2024/4/7 14:34
 * @description: TODO
 */
@Slf4j
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public
    DefaultSqlSession(Configuration configuration, Executor executor) {
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
        return executor.query(configuration, mappedStatement, params);
    }

    @Override
    public Integer insert(String statementId, Object params) throws SQLException {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(configuration, mappedStatement, params);
    }

    @Override
    public Integer update(String statementId, Object params) throws SQLException {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(configuration, mappedStatement, params);
    }

    @Override
    public Integer delete(String statementId, Object params) throws SQLException {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(configuration, mappedStatement, params);
    }

    @Override
    public <T> T getMapper(Class<T> type) {

        Object proxyInstance = Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            //方法名
            String methodName = method.getName();
            //接口全限定名
            String className = method.getDeclaringClass().getName();
            String statementId = className + "." + methodName;
            //获取方法被调用的返回值类型
            Type genericReturnType = method.getGenericReturnType();

            MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

            if (SqlCommandType.INSERT.name().equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
                return insert(statementId, args);
            } else if (SqlCommandType.UPDATE.name().equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
                return update(statementId, args);
            } else if (SqlCommandType.DELETE.name().equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
                return delete(statementId, args);
            }
            return genericReturnType instanceof ParameterizedType ?
                    selectList(statementId, args) : selectOne(statementId, args);
        });
        return (T) proxyInstance;
    }
}
