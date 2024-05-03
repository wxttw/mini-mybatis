package com.wxttw.frameworks.mybatis.configuration.transaction.jdbc;

import com.wxttw.frameworks.mybatis.configuration.transaction.Transaction;
import com.wxttw.frameworks.mybatis.configuration.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author jay
 * @date 2024/5/2 19:18
 * @description: TODO
 */
public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, int level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
