package com.wxttw.frameworks.mybatis.configuration.binding;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import com.wxttw.frameworks.mybatis.session.SqlSession;
import com.wxttw.frameworks.mybatis.util.SqlCommandType;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;

/**
 * @author jay
 * @date 2024/5/3 21:27
 * @description: TODO
 */
public class MapperMethod {

    private final Class<?> mapperInterface;
    private final Method method;
    private final Configuration configuration;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.mapperInterface = mapperInterface;
        this.method = method;
        this.configuration = configuration;
    }

    public Object execute(SqlSession sqlSession, Object[] args) throws SQLException {
        //方法名
        String methodName = method.getName();
        //接口全限定名
        String className = method.getDeclaringClass().getName();
        String statementId = className + "." + methodName;
        //获取方法被调用的返回值类型
        Type genericReturnType = method.getGenericReturnType();

        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statementId);

        if (SqlCommandType.INSERT.name().equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
            return sqlSession.insert(statementId, args);
        } else if (SqlCommandType.UPDATE.name().equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
            return sqlSession.update(statementId, args);
        } else if (SqlCommandType.DELETE.name().equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
            return sqlSession.delete(statementId, args);
        }
        return genericReturnType instanceof ParameterizedType ?
                sqlSession.selectList(statementId, args) : sqlSession.selectOne(statementId, args);
    }
}
