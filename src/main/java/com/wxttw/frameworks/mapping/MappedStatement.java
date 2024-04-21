package com.wxttw.frameworks.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jay
 * @date 2024/4/5 23:41
 * @description: 主要封装xxxMapper.xml文件被解析后的SQL语句的CRUD标签内容
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MappedStatement {

    private String id;
    private Class<?> parameterTypeClass;
    private Class<?> resultTypeClass;
    private String statementType;
    private SqlSource sqlSource;
}
