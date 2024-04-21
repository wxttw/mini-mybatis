package com.wxttw.frameworks.executor;

import com.wxttw.frameworks.configuration.Configuration;
import com.wxttw.frameworks.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/14 11:47
 * @description: TODO
 */
public interface Executor {

    <T> List<T> selectList(Configuration configuration, MappedStatement mappedStatement, Object params) throws SQLException;
}
