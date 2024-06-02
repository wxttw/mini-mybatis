package com.wxttw.frameworks.mybatis.configuration.binding;

import com.wxttw.frameworks.mybatis.configuration.Configuration;
import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import com.wxttw.frameworks.mybatis.session.SqlSession;
import com.wxttw.frameworks.mybatis.util.SqlCommandType;
import lombok.Getter;

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

    private final SqlCommand command;
    private final MethodSignature method;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(mapperInterface, method, configuration);
        this.method = new MethodSignature(method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) throws SQLException {

        Object result;
        switch (command.getType()) {
            case INSERT: {
                result = sqlSession.insert(command.getName(), args);
                break;
            }
            case UPDATE: {
                result = sqlSession.update(command.getName(), args);
                break;
            }
            case DELETE: {
                result = sqlSession.delete(command.getName(), args);
                break;
            }
            case SELECT: {
                if (method.returnsMany) {
                    result = sqlSession.selectList(command.getName(), args);
                } else {
                    result = sqlSession.selectOne(command.getName(), args);
                }
                break;
            }
            default:
                throw new RuntimeException("Unknown execution method for: " + command.getName());
        }
        return result;
    }

    @Getter
    public static class SqlCommand {
        private final String name;
        private final SqlCommandType type;

        public SqlCommand(Class<?> mapperInterface, Method method, Configuration configuration) {

            String statementId = mapperInterface.getName() + "." + method.getName();
            MappedStatement mappedStatement = configuration.getMappedStatement(statementId);

            name = mappedStatement.getId();
            type = mappedStatement.getSqlCommandType();
        }
    }

    @Getter
    public static class MethodSignature {
        private final boolean returnsMany;
        private final Type returnType;

        public MethodSignature(Method method) {
            this.returnType = method.getGenericReturnType();
            this.returnsMany = this.returnType instanceof ParameterizedType;
        }
    }
}
