package com.wxttw.frameworks.mybatis.session;

import com.wxttw.frameworks.mybatis.configuration.Configuration;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/7 14:14
 * @description: TODO
 */
public interface SqlSession {

    <E> E selectOne(String statementId, Object obj) throws SQLException;

    <E> List<E> selectList(String statementId, Object obj) throws SQLException;

    Integer insert(String statementId, Object obj) throws SQLException;

    Integer update(String statementId, Object obj) throws SQLException;

    Integer delete(String statementId, Object obj) throws SQLException;

    void commit();

    void commit(boolean force);

    void rollback();

    void rollback(boolean force);

    void close();

    Configuration getConfiguration();

    <T> T getMapper(Class<T> type);
}
