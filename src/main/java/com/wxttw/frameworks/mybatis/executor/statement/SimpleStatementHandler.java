package com.wxttw.frameworks.mybatis.executor.statement;

import com.wxttw.frameworks.mybatis.executor.Executor;
import com.wxttw.frameworks.mybatis.executor.resultset.ResultSetHandler;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date 2024/6/2 18:08
 * @description: TODO
 */
public class SimpleStatementHandler extends BaseStatementHandler {

    public SimpleStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject) {
        super(executor, mappedStatement, parameterObject);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {

    }

    @Override
    public int update(Statement statement) throws SQLException {
        String sql = boundSql.getOriginalSql();
        statement.execute(sql);
        return statement.getUpdateCount();
    }

    @Override
    public <E> List<E> query(Statement statement) throws SQLException {
        String sql = boundSql.getOriginalSql();
        statement.execute(sql);
        return resultSetHandler.handleResultSets(statement);
    }

    private String getParamenteTypeString(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class ||
                clazz == Integer.class || clazz == Long.class || clazz == Double.class ||
                clazz == Float.class || clazz == Boolean.class || clazz == Byte.class ||
                clazz == Short.class || clazz == Character.class ?
                "BASIC" : "OBJECT";
    }
}
