package com.wxttw.frameworks.mybatis.configuration;

import com.wxttw.frameworks.mybatis.configuration.binding.MapperRegistry;
import com.wxttw.frameworks.mybatis.configuration.transaction.Transaction;
import com.wxttw.frameworks.mybatis.configuration.transaction.TransactionFactory;
import com.wxttw.frameworks.mybatis.configuration.transaction.jdbc.JdbcTransactionFactory;
import com.wxttw.frameworks.mybatis.executor.Executor;
import com.wxttw.frameworks.mybatis.executor.SimpleExecutor;
import com.wxttw.frameworks.mybatis.executor.parameter.DefaultParameterHandler;
import com.wxttw.frameworks.mybatis.executor.parameter.ParameterHandler;
import com.wxttw.frameworks.mybatis.executor.resultset.DefaultResultSetHandler;
import com.wxttw.frameworks.mybatis.executor.resultset.ResultSetHandler;
import com.wxttw.frameworks.mybatis.executor.statement.RoutingStatementHandler;
import com.wxttw.frameworks.mybatis.executor.statement.StatementHandler;
import com.wxttw.frameworks.mybatis.mapping.BoundSql;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import com.wxttw.frameworks.mybatis.session.SqlSession;
import com.wxttw.frameworks.mybatis.type.TypeAliasRegistry;

import com.wxttw.frameworks.mybatis.type.TypeHandlerRegistry;
import com.wxttw.frameworks.mybatis.util.ExecutorType;
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

    protected ExecutorType defaultExecutorType = ExecutorType.SIMPLE;
    private TransactionFactory transactionFactory;
    private BasicDataSource dataSource;

    private final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    private final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
    private final MapperRegistry mapperRegistry = new MapperRegistry(this);
    private final Map<String, MappedStatement> mappedStatementMap = new HashMap<>();


    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(String statementId, MappedStatement mappedStatement) {
        this.mappedStatementMap.put(statementId, mappedStatement);
    }

    public MappedStatement getMappedStatement(String id) {
        return this.mappedStatementMap.get(id);
    }

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement) {
        return new DefaultResultSetHandler(executor, mappedStatement);
    }

    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject) {
        return new RoutingStatementHandler(executor, mappedStatement, parameterObject);
    }

    public Executor newExecutor(Transaction transaction) {
        return newExecutor(transaction, defaultExecutorType);
    }

    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
        executorType = executorType == null ? defaultExecutorType : executorType;
        //可以根据情况，扩展更多executor
        return new SimpleExecutor(this, transaction);
    }
}
