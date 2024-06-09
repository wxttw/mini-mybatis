package com.wxttw.frameworks.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author jay
 * @date 2024/6/9 19:31
 * @description: TODO
 */
public class IntegerTypeHandler implements TypeHandler<Integer>{
    @Override
    public void setParameter(PreparedStatement ps, int i, Integer parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter);
    }
}
