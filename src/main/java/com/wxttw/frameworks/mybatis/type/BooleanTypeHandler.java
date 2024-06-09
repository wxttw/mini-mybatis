package com.wxttw.frameworks.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author jay
 * @date 2024/6/9 19:28
 * @description: TODO
 */
public class BooleanTypeHandler implements TypeHandler<Boolean> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        ps.setBoolean(i, parameter);
    }
}
