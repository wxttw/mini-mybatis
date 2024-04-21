package com.wxttw.frameworks.mybatis.configuration;

import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.dbcp.BasicDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jay
 * @date 2024/4/5 23:50
 * @description: TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    private BasicDataSource dataSource;

    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

    public void addMappedStatement(String statementId, MappedStatement mappedStatement) {
        this.mappedStatementMap.put(statementId, mappedStatement);
    }
}
