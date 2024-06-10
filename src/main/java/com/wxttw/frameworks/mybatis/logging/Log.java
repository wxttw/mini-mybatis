package com.wxttw.frameworks.mybatis.logging;

/**
 * @author jay
 * @date 2024/6/9 23:42
 * @description: TODO
 */
public interface Log {
    boolean isDebugEnabled();

    boolean isTraceEnabled();

    void error(String s, Throwable e);

    void error(String s);

    void debug(String s);

    void trace(String s);

    void warn(String s);
}
