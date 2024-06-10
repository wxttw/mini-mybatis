package com.wxttw.frameworks.mybatis.session.defaults;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.executor.Executor;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import com.wxttw.frameworks.mybatis.session.SqlSession;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/7 14:34
 * @description: TODO
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;
    private final Executor executor;
    private final boolean autoCommit;
    private boolean dirty;

    public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
        this.configuration = configuration;
        this.executor = executor;
        this.dirty = false;
        this.autoCommit = autoCommit;
    }

    @Override
    public <T> T selectOne(String statementId, Object params) throws SQLException {

        List<T> list = this.selectList(statementId, params);

        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException(
                    "通过selectOne()方法，期待返回一个结果, 但实际返回结果数为: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public <T> List<T> selectList(String statementId, Object params) throws SQLException {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.query(mappedStatement, params);
    }

    @Override
    public Integer insert(String statementId, Object params) throws SQLException {
        return update(statementId, params);
    }

    @Override
    public Integer update(String statementId, Object params) throws SQLException {
        dirty = true;
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(mappedStatement, params);
    }

    @Override
    public Integer delete(String statementId, Object params) throws SQLException {
        return update(statementId, params);
    }

    @Override
    public void commit() {
        commit(false);
    }

    @Override
    public void commit(boolean force) {
        try {
            executor.commit(isCommitOrRollbackRequired(force));
            dirty = false;
        } catch (Exception e) {
            throw new RuntimeException("Error committing transaction.  Cause: " + e, e);
        }
    }

    @Override
    public void rollback() {
        rollback(false);
    }

    @Override
    public void rollback(boolean force) {
        try {
            executor.rollback(isCommitOrRollbackRequired(force));
            dirty = false;
        } catch (Exception e) {
            throw new RuntimeException("Error rolling back transaction.  Cause: " + e, e);
        }
    }

    @Override
    public void close() {
        executor.close(isCommitOrRollbackRequired(false));
        dirty = false;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    private boolean isCommitOrRollbackRequired(boolean force) {
        return !autoCommit && dirty || force;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }
}
