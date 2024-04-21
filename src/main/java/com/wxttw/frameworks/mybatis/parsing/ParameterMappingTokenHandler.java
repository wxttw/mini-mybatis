package com.wxttw.frameworks.mybatis.parsing;

import com.wxttw.frameworks.mybatis.mapping.ParameterMapping;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/14 12:32
 * @description: TODO
 */

@Data
public class ParameterMappingTokenHandler implements TokenHandler {

    private List<ParameterMapping> parameterMappings = new ArrayList<>();
    @Override
    public String handleToken(String content) {
        parameterMappings.add(new ParameterMapping(content));
        return "?";
    }
}
