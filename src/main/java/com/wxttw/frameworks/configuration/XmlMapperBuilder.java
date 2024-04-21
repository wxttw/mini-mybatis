package com.wxttw.frameworks.configuration;

import com.wxttw.frameworks.mapping.MappedStatement;
import com.wxttw.frameworks.mapping.SqlSource;
import com.wxttw.frameworks.util.ClassUtil;
import com.wxttw.frameworks.util.DocumentReader;
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
            String id = this.namespace + "." + element.attributeValue("id");
            String statementType = element.attributeValue("statementType");
            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");

            configuration.addMappedStatement(id, MappedStatement.builder()
                    .id(id)
                    .statementType(statementType)
                    .parameterTypeClass(ClassUtil.getClazz(parameterType))
                    .resultTypeClass(ClassUtil.getClazz(resultType))
                    .sqlSource(new SqlSource(element.getTextTrim()))
                    .build());
        });

    }
}
