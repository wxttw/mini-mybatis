package com.wxttw.frameworks.mybatis.configuration.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author jay
 * @date 2024/5/2 19:12
 * @description: TODO
 */
public interface Transaction {


    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;

    Integer getTimeout() throws SQLException;
}
