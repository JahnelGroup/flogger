package com.jahnelgroup.flogger.spi;

import com.jahnelgroup.flogger.BindReturn;
import com.jahnelgroup.flogger.BindReturnAspect;
import com.jahnelgroup.flogger.FloggerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;

import static com.jahnelgroup.flogger.utils.ReflectionUtils.getMethod;

@Aspect
public class BindReturnAspectImpl implements BindReturnAspect {

    @Override
    @Pointcut("execution(* *(..)) && @annotation(BindReturn)")
    public void anyMethodAnnotatedWithBindReturn(BindReturn BindReturn) {
    }

    @Override
    @AfterReturning(value = "anyMethodAnnotatedWithBindReturn(BindReturn)", argNames = "jp,retVal,BindReturn", returning = "retVal")
    public void addBoundReturnToMDC(JoinPoint jp, Object retVal, BindReturn BindReturn) throws FloggerException {
        BindReturn bindReturn = getMethod(jp).getDeclaredAnnotation(BindReturn.class);
        // Add the toString() value to the MDC
        MDC.put(bindReturn.value(), retVal.toString());
    }
}
