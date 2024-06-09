package com.wxttw.frameworks.mybatis.executor.parameter;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.mapping.BoundSql;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import com.wxttw.frameworks.mybatis.mapping.ParameterMapping;
import com.wxttw.frameworks.mybatis.type.JdbcType;
import com.wxttw.frameworks.mybatis.type.TypeHandler;
import com.wxttw.frameworks.mybatis.type.TypeHandlerRegistry;
import com.wxttw.frameworks.mybatis.util.ClassUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author jay
 * @date 2024/6/9 21:50
 * @description: TODO
 */
public class DefaultParameterHandler implements ParameterHandler {

    private final TypeHandlerRegistry typeHandlerRegistry;

    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private final BoundSql boundSql;
    private final Configuration configuration;

    public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
    }

    @Override
    public Object getParameterObject() {
        return parameterObject;
    }

    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (Objects.nonNull(parameterMappings)) {
            IntStream.range(0, parameterMappings.size())
                    .forEach(i -> {
                        ParameterMapping parameterMapping = parameterMappings.get(i);
                        String propertyName = parameterMapping.getProperty();
                        Object value;
                        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                            value = parameterObject;
                        } else {
                            value = ClassUtil.getPrivateFieldValue(parameterObject.getClass(), parameterObject, propertyName);
                        }
                        JdbcType jdbcType = parameterMapping.getJdbcType();
                        TypeHandler typeHandler = parameterMapping.getTypeHandler();
                        try {
                            typeHandler.setParameter(ps, i + 1, value, jdbcType);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
