package com.wxttw.frameworks.mybatis.configuration;

import com.wxttw.frameworks.mybatis.configuration.transaction.TransactionFactory;
import com.wxttw.frameworks.mybatis.io.Resources;
import com.wxttw.frameworks.mybatis.logging.Log;
import com.wxttw.frameworks.mybatis.type.TypeAliasRegistry;
import com.wxttw.frameworks.mybatis.util.DocumentReader;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author jay
 * @date 2024/4/7 13:12
 * @description: TODO
 */
public class XmlConfigBuilder {

    private InputStream inputStream;
    private Configuration configuration;
    private TypeAliasRegistry typeAliasRegistry;

    public XmlConfigBuilder(InputStream inputStream) {
        this.inputStream = inputStream;
        this.configuration = new Configuration();
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
    }

    public Configuration parse(InputStream inputStream) {
        Document document = DocumentReader.createDocument(inputStream);
        parseConfiguration(document.getRootElement());

        return configuration;
    }

    public Configuration parse() {
        return parse(inputStream);
    }

    private void parseConfiguration(Element element) {

        parseSettings(element.element("settings"));
        //解析<environments>
        parseEnvironments(element.element("environments"));
        //解析<mappers>
        parseMappers(element.element("mappers"));
    }

    private void parseSettings(Element element) {

        Properties props = new Properties();
        element.elements("setting").forEach(ele -> {
            props.setProperty(ele.attributeValue("name"), ele.attributeValue("value"));
        });

        Class<? extends Log> logImpl = (Class<? extends Log>)resolveClass(props.getProperty("logImpl"));
        configuration.setLogImpl(logImpl);
    }

    private void parseEnvironments(Element element) {

        String attr = element.attributeValue("default");
        List<Element> elements = element.elements("environment");

        if (attr != null) {
            for (Element ele : elements) {
                String eleId = ele.attributeValue("id");
                if (eleId != null && eleId.equals(attr)) {
                    parseTransactionManager(ele.element("transactionManager"));
                    parseDataSource(ele.element("dataSource"));
                    break;
                }
            }
        } else {
            throw new RuntimeException("environments标签的default属性不能为空");
        }
    }

    private void parseTransactionManager(Element element) {
        String type = element.attributeValue("type");
        if (StringUtils.isBlank(type))
            type = "JDBC";

        try {
            TransactionFactory factory = (TransactionFactory) resolveClass(type)
                    .getDeclaredConstructor().newInstance();
            configuration.setTransactionFactory(factory);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving class. Cause: " + e, e);
        }

    }

    private void parseDataSource(Element element) {

        String type = element.attributeValue("type");
        if (StringUtils.isBlank(type))
            type = "DBCP";

        Properties prop = new Properties();
        element.elements("property").forEach(ele -> {
            prop.setProperty(ele.attributeValue("name"), ele.attributeValue("value"));
        });

        //封装数据源对象
        BasicDataSource dataSource = null;
        if (type.equals("DBCP")) {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(prop.getProperty("driver"));
            dataSource.setUrl(prop.getProperty("url"));
            dataSource.setUsername(prop.getProperty("username"));
            dataSource.setPassword(prop.getProperty("password"));
        }
        configuration.setDataSource(dataSource);
    }


    private void parseMappers(Element element) {
        element.elements("mapper").forEach(this::parseMapper);
    }

    private void parseMapper(Element element) {
        String resource = element.attributeValue("resource");
        XmlMapperBuilder parser = new XmlMapperBuilder(Resources.getResourceAsStream(resource), configuration);
        parser.parse();
    }

    private <T> Class<? extends T> resolveClass(String alias) {
        return Objects.isNull(alias) ? null : typeAliasRegistry.resolveAlias(alias);
    }
}
