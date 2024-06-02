package com.wxttw.frameworks.mybatis.session.defaults;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.configuration.transaction.Transaction;
import com.wxttw.frameworks.mybatis.configuration.transaction.TransactionFactory;
import com.wxttw.frameworks.mybatis.configuration.transaction.jdbc.JdbcTransactionFactory;
import com.wxttw.frameworks.mybatis.executor.Executor;
import com.wxttw.frameworks.mybatis.session.SqlSession;
import com.wxttw.frameworks.mybatis.session.SqlSessionFactory;

import java.sql.SQLException;

/**
 * @author jay
 * @date 2024/4/7 15:33
 * @description: TODO
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return openSessionFromDataSource(-1, true);
    }

    @Override
    public SqlSession openSession(boolean autoCommit) {
        return openSessionFromDataSource(-1, autoCommit);
    }

    private SqlSession openSessionFromDataSource(int level, boolean autoCommit) {
        Transaction tx = null;
        try {
            final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment();
            tx = transactionFactory.newTransaction(configuration.getDataSource(), level, autoCommit);
            final Executor executor = configuration.newExecutor(tx);
            return new DefaultSqlSession(configuration, executor, autoCommit);
        } catch (Exception e) {
            closeTransaction(tx); // may have fetched a connection so lets call close()
            throw new RuntimeException("Error opening session.  Cause: " + e, e);
        }
    }

    private TransactionFactory getTransactionFactoryFromEnvironment() {
        return new JdbcTransactionFactory();
    }

    private void closeTransaction(Transaction tx) {
        if (tx != null) {
            try {
                tx.close();
            } catch (SQLException ignore) {
                // Intentionally ignore. Prefer previous error.
            }
        }
    }
}
