package com.jahnelgroup.flogger.spi;

import com.jahnelgroup.flogger.BindReturn;
import com.jahnelgroup.flogger.BindReturnAspect;
import com.jahnelgroup.flogger.BindingAspect;
import com.jahnelgroup.flogger.FloggerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

import static com.jahnelgroup.flogger.utils.ReflectionUtils.getMethodFromJointPoint;

@Aspect
public class BindReturnAspectImpl extends BindingAspect implements BindReturnAspect {

    @Override
    @Pointcut("execution(* *(..)) && @annotation(BindReturn)")
    public void anyMethodAnnotatedWithBindReturn(BindReturn BindReturn) {
    }

    @Override
    @AfterReturning(value = "anyMethodAnnotatedWithBindReturn(BindReturn)", argNames = "jp,retVal,BindReturn", returning = "retVal")
    public void addBoundReturnToMDC(JoinPoint jp, Object retVal, BindReturn BindReturn) throws FloggerException {
        Method method = getMethodFromJointPoint(jp);
        BindReturn bindReturn = method.getDeclaredAnnotation(BindReturn.class);
        if (bindReturn.expand()) {
            expandAndPut(retVal);
        } else {
            if (!bindReturn.value().isEmpty()) {
                put(bindReturn.value(), retVal.toString());
            } else {
                put(method.getName(), retVal.toString());
            }
        }
    }
}
