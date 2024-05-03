package com.wxttw.frameworks.mybatis.executor;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.configuration.transaction.Transaction;
import com.wxttw.frameworks.mybatis.mapping.BoundSql;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import com.wxttw.frameworks.mybatis.mapping.ParameterMapping;
import com.wxttw.frameworks.mybatis.util.ClassUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author jay
 * @date 2024/4/14 11:54
 * @description: TODO
 */
@Slf4j
public class SimpleExecutor implements Executor {

    private Configuration configuration;
    private Transaction transaction;

    private boolean closed = false;

    public SimpleExecutor(Configuration configuration, Transaction transaction){
        this.configuration = configuration;
        this.transaction = transaction;
    }


    @Override
    public <T> List<T> query(MappedStatement mappedStatement, Object params) throws SQLException {
        Connection connection = getConnection();
        return handleStatementSelect(connection, mappedStatement, params);
    }

    @Override
    public Integer update(MappedStatement mappedStatement, Object params) throws SQLException {
        Connection connection = getConnection();
        return handleStatementUpdate(connection, mappedStatement, params);
    }

    private Connection getConnection() throws SQLException {
        return transaction.getConnection();
    }

    private <E> List<E> handleStatementSelect(Connection connection, MappedStatement mappedStatement, Object params) throws SQLException {
        String statementType = mappedStatement.getStatementType();
        List<Object> result = null;
        //不同statementType会有不同操作
        if ("prepared".equals(statementType)) {
            result = handlePreparedStatementSelect(connection, mappedStatement, params);
        } else {
            //other ....
        }

        return (List<E>) result;
    }

    private Integer handleStatementUpdate(Connection connection, MappedStatement mappedStatement, Object params) throws SQLException {

        try {
            BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql();
            String sql = boundSql.getOriginalSql();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //获得参数类型
            Class<?> parameterTypeClass = mappedStatement.getParameterTypeClass();
            //获得参数名称
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

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
            return preparedStatement.executeUpdate();

        } catch (Exception e) {
            // TODO: handle exception
            log.error("handleStatementUpdate-exception: {}", e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * JDBC操作
     *
     * @param connection
     * @param mappedStatement
     * @param params
     * @throws SQLException
     */
    private <E> List<E> handlePreparedStatementSelect(Connection connection, MappedStatement mappedStatement, Object params) throws SQLException {

        List<Object> result = new ArrayList<Object>();
        try {
            BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql();
            String sql = boundSql.getOriginalSql();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //获得参数类型
            Class<?> parameterTypeClass = mappedStatement.getParameterTypeClass();

            if (Objects.nonNull(parameterTypeClass)) {
                //获得参数名称
                List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

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


            //TODO:resultset handler
            Class<?> resultTypeClass = mappedStatement.getResultTypeClass();
            ResultSet resultSet = preparedStatement.executeQuery();

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
            // TODO: handle exception
            log.error("handlePreparedStatementSelect-exception: {}", e.getMessage());
        }
        return (List<E>) result;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed) {
            throw new RuntimeException("Cannot commit, transaction is already closed");
        }
        if (required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed && required) {
            transaction.rollback();
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                if (transaction != null) {
                    transaction.close();
                }
            }
        } catch (SQLException e) {
            log.warn("Unexpected exception on closing transaction.  Cause: " + e);
        } finally {
            transaction = null;
            closed = true;
        }
    }

    private String getParamenteTypeString(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class ||
                clazz == Integer.class || clazz == Long.class || clazz == Double.class ||
                clazz == Float.class || clazz == Boolean.class || clazz == Byte.class ||
                clazz == Short.class || clazz == Character.class ?
                "BASIC" : "OBJECT";
    }
}
