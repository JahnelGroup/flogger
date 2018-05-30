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

import static com.jahnelgroup.flogger.utils.ReflectionUtils.getMethodFromJointPoint;
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
        Method method = getMethodFromJointPoint(jp);
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = getParameterNames(parameters);
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(BindParam.class)) {
                BindParam bindParam = parameters[i].getAnnotation(BindParam.class);
                if (bindParam.expand()) {
                    expandAndPut(args[i]);
                } else {
                    if (!bindParam.value().isEmpty()) {
                        put(bindParam.value(), args[i].toString());
                    } else {
                        put(parameterNames[i], args[i].toString());
                    }
                }
            }
        }
    }
}
