package com.jahnelgroup.flogger;

import com.jahnelgroup.flogger.spi.BindParamAspectImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Log4j2BindParamAspectImpl extends BindParamAspectImpl {

    private static final Logger logger = LogManager.getLogger(Log4j2BindParamAspectImpl.class);

    @Pointcut("execution(* *(.., @com.jahnelgroup.flogger.BindParam (*), ..))")
    public void anyMethodWithBindParams() {}

    @Override
    public void put(String key, String value) {
        logger.info("testing put [ " + key + " : " + value + " ]");
        ThreadContext.put(key, value);
    }
}
