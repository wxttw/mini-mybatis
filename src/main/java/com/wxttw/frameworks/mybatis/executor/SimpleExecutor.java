package com.wxttw.frameworks.mybatis.executor;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.configuration.transaction.Transaction;
import com.wxttw.frameworks.mybatis.executor.statement.StatementHandler;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/14 11:54
 * @description: TODO
 */
@Slf4j
public class SimpleExecutor implements Executor {

    private Configuration configuration;
    private Transaction transaction;

    private boolean closed = false;

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
    }

    @Override
    public <T> List<T> query(MappedStatement mappedStatement, Object params) throws SQLException {
        Statement stmt = null;
        try {
            Configuration configuration = mappedStatement.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this, mappedStatement);
            stmt = prepareStatement(handler, params);
            return handler.<T>query(stmt);
        } finally {
            closeStatement(stmt);
        }
    }

    @Override
    public Integer update(MappedStatement mappedStatement, Object params) throws SQLException {
        Statement stmt = null;
        try {
            Configuration configuration = mappedStatement.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this, mappedStatement);
            stmt = prepareStatement(handler, params);
            return handler.update(stmt);
        } finally {
            closeStatement(stmt);
        }
    }

    private Connection getConnection() throws SQLException {
        return transaction.getConnection();
    }

    private Statement prepareStatement(StatementHandler handler, Object params) throws SQLException {
        Statement stmt;
        Connection connection = getConnection();
        stmt = handler.prepare(connection, transaction.getTimeout());
        handler.parameterize(stmt, params);
        return stmt;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed) {
            throw new RuntimeException("Cannot commit, transaction is already closed");
        }
        if (required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed && required) {
            transaction.rollback();
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                if (transaction != null) {
                    transaction.close();
                }
            }
        } catch (SQLException e) {
            log.warn("Unexpected exception on closing transaction.  Cause: " + e);
        } finally {
            transaction = null;
            closed = true;
        }
    }

    protected void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
}
