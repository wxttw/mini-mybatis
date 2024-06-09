package com.wxttw.frameworks.mybatis.parsing;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericTokenParser {

    private String pattern;
    private final TokenHandler handler;

    public GenericTokenParser(String pattern, TokenHandler handler) {
        this.pattern = pattern;
        this.handler = handler;
    }

    public String parse(String sql) {
        Pattern pattern = Pattern.compile(this.pattern);
        Matcher matcher = pattern.matcher(sql);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, handler.handleToken(matcher.group(1)));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
