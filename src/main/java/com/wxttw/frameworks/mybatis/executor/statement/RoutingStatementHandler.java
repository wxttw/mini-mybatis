package com.wxttw.frameworks.mybatis.executor.statement;

import com.wxttw.frameworks.mybatis.executor.Executor;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author jay
 * @date 2024/6/2 17:47
 * @description: TODO
 */
public class RoutingStatementHandler implements StatementHandler {

    private final StatementHandler delegate;

    public RoutingStatementHandler(Executor executor, MappedStatement ms) {
        switch (ms.getStatementType()) {
            case STATEMENT:
                delegate = new SimpleStatementHandler(executor, ms);
                break;
            case PREPARED:
                delegate = new PreparedStatementHandler(executor, ms);
                break;
            case CALLABLE:
                //TODO: 暂不实现
            default:
                throw new RuntimeException("Unknown statement type: " + ms.getStatementType());
        }
    }

    @Override
    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
        return delegate.prepare(connection, transactionTimeout);
    }

    @Override
    public void parameterize(Statement statement, Object params) throws SQLException {
        delegate.parameterize(statement, params);
    }

    @Override
    public int update(Statement statement) throws SQLException {
        return delegate.update(statement);
    }

    @Override
    public <E> List<E> query(Statement statement) throws SQLException {
        return delegate.<E>query(statement);
    }
}
