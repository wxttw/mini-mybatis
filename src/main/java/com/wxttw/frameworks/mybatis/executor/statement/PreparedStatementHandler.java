package com.wxttw.frameworks.mybatis.executor.statement;

import com.wxttw.frameworks.mybatis.executor.Executor;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import com.wxttw.frameworks.mybatis.mapping.ParameterMapping;
import com.wxttw.frameworks.mybatis.util.ClassUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author jay
 * @date 2024/6/2 18:12
 * @description: TODO
 */
public class PreparedStatementHandler extends BaseStatementHandler {

    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement) {
        super(executor, mappedStatement);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(boundSql.getOriginalSql());
    }

    @Override
    public void parameterize(Statement statement, Object params) throws SQLException {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        Class<?> parameterTypeClass = mappedStatement.getParameterTypeClass();
        //获得参数名称
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        if (Objects.nonNull(parameterTypeClass)) {
            if ("BASIC".equals(getParamenteTypeString(parameterTypeClass))) {
                preparedStatement.setObject(1, params);
            } else {
                //Object
                for (int i = 0; i < parameterMappings.size(); i++) {
                    ParameterMapping parameterMapping = parameterMappings.get(i);
                    String fieldName = parameterMapping.getContent();

                    Object fieldValue = ClassUtil.getPrivateFieldValue(parameterTypeClass, params, fieldName);
                    preparedStatement.setObject(i + 1, fieldValue);
                }
            }
        }
    }

    @Override
    public int update(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return ps.getUpdateCount();
    }

    @Override
    public <E> List<E> query(Statement statement) throws SQLException {
        List<Object> result = new ArrayList<Object>();

        try {
            PreparedStatement ps = (PreparedStatement) statement;
            ps.execute();

            Class<?> resultTypeClass = mappedStatement.getResultTypeClass();
            ResultSet resultSet = ps.executeQuery();

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

    private String getParamenteTypeString(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class ||
                clazz == Integer.class || clazz == Long.class || clazz == Double.class ||
                clazz == Float.class || clazz == Boolean.class || clazz == Byte.class ||
                clazz == Short.class || clazz == Character.class ?
                "BASIC" : "OBJECT";
    }
}
