package com.wxttw.frameworks.mybatis.configuration;

import com.wxttw.frameworks.mybatis.io.Resources;
import com.wxttw.frameworks.mybatis.util.DocumentReader;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * @author jay
 * @date 2024/4/7 13:12
 * @description: TODO
 */
public class XmlConfigBuilder {

    private InputStream inputStream;
    private Configuration configuration;

    public XmlConfigBuilder(InputStream inputStream) {
        this.inputStream = inputStream;
        this.configuration = new Configuration();
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

        //解析<environments>
        parseEnvironments(element.element("environments"));
        //解析<mappers>
        parseMappers(element.element("mappers"));
    }

    private void parseEnvironments(Element element) {

        String attr = element.attributeValue("default");
        List<Element> elements = element.elements("environment");

        if (attr != null) {
            for (Element ele : elements) {
                String eleId = ele.attributeValue("id");
                if (eleId != null && eleId.equals(attr)) {
                    parseDataSource(ele.element("dataSource"));

                    break;
                }
            }
        } else {
            throw new RuntimeException("environments标签的default属性不能为空");
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
}
