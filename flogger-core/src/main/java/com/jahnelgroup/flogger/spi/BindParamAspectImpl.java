package com.jahnelgroup.flogger.spi;

import com.jahnelgroup.flogger.BindParam;
import com.jahnelgroup.flogger.BindParamAspect;
import com.jahnelgroup.flogger.FloggerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static com.jahnelgroup.flogger.utils.ReflectionUtils.getMethod;
import static com.jahnelgroup.flogger.utils.ReflectionUtils.getParameterNames;

@Aspect
public class BindParamAspectImpl implements BindParamAspect {

    private static final Logger logger = LoggerFactory.getLogger(BindParamAspectImpl.class);

    @Override
    @Pointcut("execution(* *(.., @com.jahnelgroup.flogger.BindParam (*), ..))")
    public void anyMethodWithBindParams() {}

    @Override
    @Before(value = "anyMethodWithBindParams()", argNames = "jp")
    public void addBoundParamsToMDC(JoinPoint jp) throws FloggerException {
        logger.info("ADDING BIND PARAMS TO MDC");
        Object[] args = jp.getArgs();
        Method method = getMethod(jp);
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = getParameterNames(parameters);
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(BindParam.class)) {
                BindParam anno = parameters[i].getAnnotation(BindParam.class);
                // Call toString() on the parameter and add it to the MDC
                // with key= value() on annotation or the parameter's name
                if (!anno.value().isEmpty()) {
                    MDC.put(anno.value(), args[i].toString());
                } else {
                    MDC.put(parameterNames[i], args[i].toString());
                }
            }
        }
    }
}
