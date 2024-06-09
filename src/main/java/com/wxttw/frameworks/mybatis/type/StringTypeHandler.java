package com.wxttw.frameworks.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author jay
 * @date 2024/6/9 19:33
 * @description: TODO
 */
public class StringTypeHandler implements TypeHandler<String>{
    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter);
    }
}
