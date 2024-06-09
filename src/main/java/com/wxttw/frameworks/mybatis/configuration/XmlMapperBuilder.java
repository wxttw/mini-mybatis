package com.wxttw.frameworks.mybatis.configuration;

import com.wxttw.frameworks.mybatis.mapping.MappedStatement;
import com.wxttw.frameworks.mybatis.mapping.SqlSource;
import com.wxttw.frameworks.mybatis.util.ClassUtil;
import com.wxttw.frameworks.mybatis.util.DocumentReader;
import com.wxttw.frameworks.mybatis.util.SqlCommandType;
import com.wxttw.frameworks.mybatis.util.StatementType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date 2024/4/5 23:56
 * @description: 解析所有的Mapper配置文件，封装所以SQL语句标签到MappedStatement对象中
 */
@Slf4j
public class XmlMapperBuilder {

    private InputStream inputStream;
    private Configuration configuration;
    private String namespace;

    public XmlMapperBuilder(InputStream inputStream, Configuration configuration) {
        this.inputStream = inputStream;
        this.configuration = configuration;
    }

    /**
     * 传入Mapper配置文件的字节流，最终封装为MappedStatement对象，保存到Configuration对象中
     */
    public void parse() {
        Document document = DocumentReader.createDocument(inputStream);
        Element rootElement = document.getRootElement();
        namespace = rootElement.attributeValue("namespace");

        if (StringUtils.isBlank(namespace)) {
            throw new RuntimeException("Mapper的namespace值不能为空");
        }

        List<Element> allElements = new ArrayList<>();
        allElements.addAll(rootElement.elements("select"));
        allElements.addAll(rootElement.elements("insert"));
        allElements.addAll(rootElement.elements("update"));
        allElements.addAll(rootElement.elements("delete"));

        allElements.forEach(element -> {
            String sqlCommandType = element.getName().toUpperCase();
            String id = this.namespace + "." + element.attributeValue("id");
            String statementType = element.attributeValue("statementType");
            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");

            SqlSource sqlSource = new SqlSource(this.configuration, element.getTextTrim(), ClassUtil.getClazz(parameterType));
            configuration.addMappedStatement(id, MappedStatement.builder()
                    .configuration(configuration)
                    .id(id)
                    .statementType(buildStatementType(statementType))
                    .parameterTypeClass(ClassUtil.getClazz(parameterType))
                    .resultTypeClass(ClassUtil.getClazz(resultType))
                    .sqlSource(sqlSource)
                    .sqlCommandType(SqlCommandType.valueOf(sqlCommandType))
                    .build());
        });

        bindMapperForNamespace();
    }

    private StatementType buildStatementType(String statementType) {
        if (StringUtils.isBlank(statementType)) {
            return StatementType.STATEMENT;
        }
        return StatementType.valueOf(statementType.toUpperCase());
    }

    private void bindMapperForNamespace() {
        Class<?> boundType = ClassUtil.getClazz(namespace);
        if (boundType != null) {
            if (!configuration.hasMapper(boundType)) {
                configuration.addMapper(boundType);
            }
        }
    }
}
