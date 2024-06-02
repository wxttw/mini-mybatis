package com.wxttw.frameworks.mybatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author jay
 * @date 2024/6/2 17:45
 * @description: TODO
 */
public interface StatementHandler {

    Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException;

    void parameterize(Statement statement, Object params) throws SQLException;

    int update(Statement statement) throws SQLException;

    <E> List<E> query(Statement statement) throws SQLException;
}
