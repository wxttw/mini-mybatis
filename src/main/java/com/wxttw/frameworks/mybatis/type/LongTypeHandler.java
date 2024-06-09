package com.wxttw.frameworks.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author jay
 * @date 2024/6/9 19:32
 * @description: TODO
 */
public class LongTypeHandler implements TypeHandler<Long> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter);
    }
}
