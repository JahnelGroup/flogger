package com.jahnelgroup.flogger;

import com.jahnelgroup.flogger.advice.MDCAdvice;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;

@Aspect
public class LogbackMDCAdvice extends MDCAdvice {

    @Override
    protected void put(String key, String value) {
        MDC.put(key, value);
    }
}
