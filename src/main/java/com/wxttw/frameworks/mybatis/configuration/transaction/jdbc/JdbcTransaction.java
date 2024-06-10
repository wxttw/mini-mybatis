package com.wxttw.frameworks.mybatis.configuration.transaction.jdbc;

import com.wxttw.frameworks.mybatis.configuration.transaction.Transaction;
import com.wxttw.frameworks.mybatis.logging.Log;
import com.wxttw.frameworks.mybatis.logging.LogFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author jay
 * @date 2024/5/2 19:17
 * @description: TODO
 */

public class JdbcTransaction implements Transaction {

    private static final Log log = LogFactory.getLog(JdbcTransaction.class);

    protected Connection connection;
    protected DataSource dataSource;
    protected boolean autoCommit;
    protected int level = -1;

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    public JdbcTransaction(DataSource ds, int desiredLevel, boolean desiredAutoCommit) {
        this.dataSource = ds;
        this.level = desiredLevel;
        this.autoCommit = desiredAutoCommit;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            openConnection();
        }
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            if (log.isDebugEnabled()) {
                log.debug("Committing JDBC Connection [" + connection + "]");
            }
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            if (log.isDebugEnabled()) {
                log.debug("Rolling back JDBC Connection [" + connection + "]");
            }
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            resetAutoCommit();
            if (log.isDebugEnabled()) {
                log.debug("Closing JDBC Connection [" + connection + "]");
            }
            connection.close();
        }
    }

    protected void setDesiredAutoCommit(boolean desiredAutoCommit) {
        try {
            if (connection.getAutoCommit() != desiredAutoCommit) {
                if (log.isDebugEnabled()) {
                    log.debug("Setting autocommit to " + desiredAutoCommit + " on JDBC Connection [" + connection + "]");
                }
                connection.setAutoCommit(desiredAutoCommit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error configuring AutoCommit.  " + "Your driver may not support getAutoCommit() or setAutoCommit(). "
                            + "Requested setting: " + desiredAutoCommit + ".  Cause: " + e,
                    e);
        }
    }

    private void resetAutoCommit() {
        try {
            if (!connection.getAutoCommit()) {
                if (log.isDebugEnabled()) {
                    log.debug("Resetting autocommit to true on JDBC Connection [" + connection + "]");
                }
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error resetting autocommit to true " + "before closing the connection.  Cause: " + e);
            }
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return 100;
    }

    protected void openConnection() throws SQLException {

        connection = dataSource.getConnection();
        if (level != -1) {
            connection.setTransactionIsolation(level);
        }
        setDesiredAutoCommit(autoCommit);
    }
}
