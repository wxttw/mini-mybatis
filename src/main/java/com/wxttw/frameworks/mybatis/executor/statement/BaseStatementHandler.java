package com.wxttw.frameworks.mybatis.executor.statement;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.executor.Executor;
import com.wxttw.frameworks.mybatis.executor.resultset.ResultSetHandler;
import com.wxttw.frameworks.mybatis.mapping.BoundSql;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author jay
 * @date 2024/6/2 17:57
 * @description: TODO
 */
public abstract class BaseStatementHandler implements StatementHandler {

    protected final Configuration configuration;
    protected final Executor executor;

    protected final ResultSetHandler resultSetHandler;
    protected final MappedStatement mappedStatement;
    protected BoundSql boundSql;


    protected BaseStatementHandler(Executor executor, MappedStatement mappedStatement) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;

        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement);
        this.boundSql = mappedStatement.getBoundSql();
    }

    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);
            statement.setQueryTimeout(transactionTimeout);
            return statement;
        } catch (SQLException e) {
            closeStatement(statement);
            throw e;
        } catch (Exception e) {
            closeStatement(statement);
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;

    protected void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            //ignore
        }
    }
}
