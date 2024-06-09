package com.wxttw.frameworks.mybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author jay
 * @date 2024/6/9 21:49
 * @description: TODO
 */
public interface ParameterHandler {
    Object getParameterObject();

    void setParameters(PreparedStatement ps)
            throws SQLException;
}
