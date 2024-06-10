package com.wxttw.frameworks.mybatis.logging;

import java.lang.reflect.Constructor;

/**
 * @author jay
 * @date 2024/6/9 23:43
 * @description: TODO
 */
public class LogFactory {

    public static final String MARKER = "MYBATIS";
    private static Constructor<? extends Log> logConstructor;

    static {
        tryImplementation(LogFactory::useSlf4jLogging);
        tryImplementation(LogFactory::useCommonsLogging);
        tryImplementation(LogFactory::useLog4JLogging);
        tryImplementation(LogFactory::useNoLogging);
    }


    private LogFactory() {
    }

    public static Log getLog(Class<?> aClass) {
        return getLog(aClass.getName());
    }

    public static Log getLog(String logger) {
        try {
            return logConstructor.newInstance(logger);
        } catch (Throwable t) {
            throw new RuntimeException("Error creating logger for logger " + logger + ".  Cause: " + t, t);
        }
    }

    public static synchronized void useCustomLogging(Class<? extends Log> clazz) {
        setImplementation(clazz);
    }

    public static synchronized void useSlf4jLogging() {
        setImplementation(com.wxttw.frameworks.mybatis.logging.slf4j.Slf4jImpl.class);
    }

    public static synchronized void useCommonsLogging() {
        setImplementation(com.wxttw.frameworks.mybatis.logging.commons.JakartaCommonsLoggingImpl.class);
    }

    public static synchronized void useLog4JLogging() {
        setImplementation(com.wxttw.frameworks.mybatis.logging.log4j.Log4jImpl.class);
    }

    public static synchronized void useStdOutLogging() {
        setImplementation(com.wxttw.frameworks.mybatis.logging.stdout.StdOutImpl.class);
    }

    public static synchronized void useNoLogging() {
        setImplementation(com.wxttw.frameworks.mybatis.logging.nologging.NoLoggingImpl.class);
    }

    private static void tryImplementation(Runnable runnable) {
        if (logConstructor == null) {
            try {
                runnable.run();
            } catch (Throwable t) {
                // ignore
            }
        }
    }

    private static void setImplementation(Class<? extends Log> implClass) {
        try {
            Constructor<? extends Log> candidate = implClass.getConstructor(String.class);
            Log log = candidate.newInstance(LogFactory.class.getName());
            if (log.isDebugEnabled()) {
                log.debug("Logging initialized using '" + implClass + "' adapter.");
            }
            logConstructor = candidate;
        } catch (Throwable t) {
            throw new RuntimeException("Error setting Log implementation.  Cause: " + t, t);
        }
    }
}
