package com.wxttw.frameworks.mybatis.configuration;

import com.wxttw.frameworks.mybatis.configuration.transaction.TransactionFactory;
import com.wxttw.frameworks.mybatis.configuration.transaction.jdbc.JdbcTransactionFactory;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import com.wxttw.frameworks.mybatis.type.TypeAliasRegistry;
import lombok.Data;
import org.apache.commons.dbcp.BasicDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jay
 * @date 2024/4/5 23:50
 * @description: TODO
 */
@Data
public class Configuration {

    private TransactionFactory transactionFactory;
    private BasicDataSource dataSource;

    private final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
    }

    public void addMappedStatement(String statementId, MappedStatement mappedStatement) {
        this.mappedStatementMap.put(statementId, mappedStatement);
    }
}
