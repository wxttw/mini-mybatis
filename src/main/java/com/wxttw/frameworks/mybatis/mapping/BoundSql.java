package com.wxttw.frameworks.mybatis.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author jay
 * @date 2024/4/14 11:51
 * @description: TODO
 */

@Data
@AllArgsConstructor
public class BoundSql {
    private String originalSql;
    private List<ParameterMapping> parameterMappings;

    public void addParameterMapping(ParameterMapping parameterMapping) {
        this.parameterMappings.add(parameterMapping);
    }
}
