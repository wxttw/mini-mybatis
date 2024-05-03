package com.wxttw.frameworks.mybatis.configuration.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author jay
 * @date 2024/5/2 19:12
 * @description: TODO
 */
public interface TransactionFactory {

    Transaction newTransaction(Connection conn);

    Transaction newTransaction(DataSource dataSource, int level, boolean autoCommit);
}
