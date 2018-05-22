package com.jahnelgroup.flogger;

import com.jahnelgroup.flogger.spi.BindParamAspectImpl;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Aspect
public class LogbackBindParamAspectImpl extends BindParamAspectImpl {

    private static Logger logger = LoggerFactory.getLogger(LogbackBindParamAspectImpl.class);

    @Pointcut("execution(* *(.., @com.jahnelgroup.flogger.BindParam (*), ..))")
    public void anyMethodWithBindParams() {}

    @Override
    public void put(String key, String value) {
        logger.info("testing put [ " + key + " : " + value + " ]");
        MDC.put(key, value);
    }
}
