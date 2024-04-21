package com.wxttw.frameworks.mapping;

import com.wxttw.frameworks.parsing.GenericTokenParser;
import com.wxttw.frameworks.parsing.ParameterMappingTokenHandler;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author jay
 * @date 2024/4/14 14:02
 * @description: TODO
 */
@Data
@AllArgsConstructor
public class SqlSource {

    private String sqlText;


    public BoundSql getBoundSql() {
        //这是从mybatis源码中直接获得的工具类，用于解析sql获得原始的sql语句
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser parser = new GenericTokenParser("#{", "}", tokenHandler);
        //封装原始的sql以及参数列表名称
        return new BoundSql(parser.parse(sqlText), tokenHandler.getParameterMappings());
    }
}
