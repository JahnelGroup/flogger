package com.jahnelgroup.flogger.spi;

import com.jahnelgroup.flogger.BindParam;
import com.jahnelgroup.flogger.BindParamAspect;
import com.jahnelgroup.flogger.BindingAspect;
import com.jahnelgroup.flogger.FloggerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static com.jahnelgroup.flogger.utils.ReflectionUtils.getMethod;
import static com.jahnelgroup.flogger.utils.ReflectionUtils.getParameterNames;

@Aspect
public class BindParamAspectImpl extends BindingAspect implements BindParamAspect {

    @Override
    @Pointcut("execution(* *(.., @com.jahnelgroup.flogger.BindParam (*), ..))")
    public void anyMethodWithBindParams() {
    }

    @Override
    @Before(value = "anyMethodWithBindParams()", argNames = "jp")
    public void addBoundParamsToMDC(JoinPoint jp) throws FloggerException {
        Object[] args = jp.getArgs();
        Method method = getMethod(jp);
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = getParameterNames(parameters);
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(BindParam.class)) {
                BindParam anno = parameters[i].getAnnotation(BindParam.class);
                // Call toString() on the parameter and add it to the MDC
                // with key= value() on annotation or the parameter's name
                if (anno.expand()) {
                    expandAndPut(args[i]);
                } else {
                    if (!anno.value().isEmpty()) {
                        put(anno.value(), args[i].toString());
                    } else {
                        put(parameterNames[i], args[i].toString());
                    }
                }
            }
        }
    }
}
