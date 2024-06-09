package com.wxttw.frameworks.mybatis.parsing;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.configuration.build.BaseBuilder;
import com.wxttw.frameworks.mybatis.mapping.ParameterMapping;
import com.wxttw.frameworks.mybatis.util.ClassUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/14 12:32
 * @description: TODO
 */


public class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

    private List<ParameterMapping> parameterMappings = new ArrayList<>();
    private Class<?> parameterType;

    public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType) {
        super(configuration);
        this.parameterType = parameterType;
    }

    @Override
    public String handleToken(String content) {
        parameterMappings.add(buildParameterMapping(content));
        return "?";
    }

    private ParameterMapping buildParameterMapping(String propertyName) { //fieldName

        Class<?> propertyType;
        if (typeHandlerRegistry.hasTypeHandler(parameterType)) {
            propertyType = parameterType;
        } else {
            propertyType = ClassUtil.getFieldType(parameterType, propertyName);
        }
        ParameterMapping parameterMapping = new ParameterMapping(configuration, propertyName, propertyType);
        return parameterMapping.build();
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }
}
