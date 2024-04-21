package com.wxttw.frameworks.mybatis.parsing;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericTokenParser {

    private final TokenHandler handler;

    public GenericTokenParser(TokenHandler handler) {
        this.handler = handler;
    }

    public String parse(String sql) {
        Pattern pattern = Pattern.compile("#\\{([^}]+)}");
        Matcher matcher = pattern.matcher(sql);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, handler.handleToken(matcher.group(1)));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
