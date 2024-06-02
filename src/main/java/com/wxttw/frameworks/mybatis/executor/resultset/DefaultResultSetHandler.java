package com.wxttw.frameworks.mybatis.executor.resultset;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.executor.Executor;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date 2024/6/2 20:49
 * @description: TODO
 */
public class DefaultResultSetHandler implements ResultSetHandler {

    private final Executor executor;
    private final Configuration configuration;
    private final MappedStatement mappedStatement;

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement) {
        this.executor = executor;
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
    }

    @Override
    public <E> List<E> handleResultSets(Statement stmt) throws SQLException {

        List<Object> result = new ArrayList<Object>();
        PreparedStatement ps = (PreparedStatement) stmt;
        try {
            Class<?> resultTypeClass = mappedStatement.getResultTypeClass();
            ResultSet resultSet = ps.getResultSet();

            while (resultSet.next()) {
                //遍历封装Result
                Object newInstance = resultTypeClass.getDeclaredConstructor().newInstance();
                int columnCount = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    Field field = resultTypeClass.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(newInstance, resultSet.getObject(columnName));
                }
                result.add(newInstance);
            }
        } catch (Exception e) {
            //log.error("handlePreparedStatementSelect-exception: {}", e.getMessage());
        }
        return (List<E>) result;
    }
}
