package com.wxttw.frameworks.mybatis.logging.nologging;

import com.wxttw.frameworks.mybatis.logging.Log;

/**
 * @author jay
 * @date 2024/6/10 11:06
 * @description: TODO
 */
public class NoLoggingImpl implements Log {
    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void error(String s, Throwable e) {

    }

    @Override
    public void error(String s) {

    }

    @Override
    public void debug(String s) {

    }

    @Override
    public void trace(String s) {

    }

    @Override
    public void warn(String s) {

    }
}
