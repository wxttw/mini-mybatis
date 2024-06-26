package com.wxttw.frameworks.mybatis.executor;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.configuration.transaction.Transaction;
import com.wxttw.frameworks.mybatis.executor.statement.StatementHandler;
import com.wxttw.frameworks.mybatis.logging.Log;
import com.wxttw.frameworks.mybatis.logging.LogFactory;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/14 11:54
 * @description: TODO
 */

public class SimpleExecutor implements Executor {

    private static final Log log = LogFactory.getLog(SimpleExecutor.class);
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
            StatementHandler handler = configuration.newStatementHandler(this, mappedStatement, params);
            stmt = prepareStatement(handler);
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
            StatementHandler handler = configuration.newStatementHandler(this, mappedStatement, params);
            stmt = prepareStatement(handler);
            return handler.update(stmt);
        } finally {
            closeStatement(stmt);
        }
    }

    private Connection getConnection() throws SQLException {
        return transaction.getConnection();
    }

    private Statement prepareStatement(StatementHandler handler) throws SQLException {
        Statement stmt;
        Connection connection = getConnection();
        stmt = handler.prepare(connection, transaction.getTimeout());
        handler.parameterize(stmt);
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
