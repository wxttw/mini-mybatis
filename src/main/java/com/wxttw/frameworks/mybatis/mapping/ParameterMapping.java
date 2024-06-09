package com.wxttw.frameworks.mybatis.mapping;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.type.JdbcType;
import com.wxttw.frameworks.mybatis.type.TypeHandler;
import com.wxttw.frameworks.mybatis.type.TypeHandlerRegistry;
import lombok.Data;

/**
 * @author jay
 * @date 2024/4/14 11:52
 * @description: TODO
 */

@Data
public class ParameterMapping {

    private Configuration configuration;

    private String property;

    private Class<?> javaType = Object.class;
    private JdbcType jdbcType;

    private TypeHandler<?> typeHandler;

    public ParameterMapping(Configuration configuration, String property, Class<?> javaType) {
        this.configuration = configuration;
        this.property = property;
        this.javaType = javaType;
    }

    public ParameterMapping build() {
        if (this.typeHandler == null && this.javaType != null) {
            Configuration configuration = this.configuration;
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            this.typeHandler = typeHandlerRegistry.getTypeHandler(this.javaType, this.jdbcType);
        }
        return this;
    }
}
