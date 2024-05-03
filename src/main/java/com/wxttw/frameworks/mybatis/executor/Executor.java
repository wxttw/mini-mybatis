package com.wxttw.frameworks.mybatis.executor;

import com.wxttw.frameworks.mybatis.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/14 11:47
 * @description: TODO
 */
public interface Executor {

    <T> List<T> query(MappedStatement mappedStatement, Object params) throws SQLException;

    Integer update(MappedStatement mappedStatement, Object params) throws SQLException;

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);
}
