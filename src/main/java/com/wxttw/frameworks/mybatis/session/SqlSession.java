package com.wxttw.frameworks.mybatis.session;

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

    /*<T> T getMapper(Class<?> type);*/
}
