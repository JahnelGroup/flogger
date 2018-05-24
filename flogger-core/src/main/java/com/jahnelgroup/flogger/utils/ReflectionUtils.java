package com.jahnelgroup.flogger.utils;

import com.jahnelgroup.flogger.FloggerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public final class ReflectionUtils {

    private static final String CLASS_NOT_FOUND_EXCEPTION_MSG = "Error finding class: %s";
    private static final String NO_SUCH_METHOD_EXCEPTION_MSG = "Error finding %s(%s) on class: %s";

    public static Method getMethod(JoinPoint joinPoint) throws FloggerException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringTypeName();
        Class[] parameterTypes = methodSignature.getParameterTypes();
        try {
            return Class.forName(className).getDeclaredMethod(methodSignature.getName(), parameterTypes);
        } catch (ClassNotFoundException e) {
            throw new FloggerException(String.format(CLASS_NOT_FOUND_EXCEPTION_MSG, className), e);
        } catch (NoSuchMethodException e) {
            throw new FloggerException(String.format(
                    NO_SUCH_METHOD_EXCEPTION_MSG, methodSignature.getName(),
                    Arrays.toString(parameterTypes).substring(1, parameterTypes.length - 1),
                    className), e);
        }
    }

    public static String[] getParameterNames(Parameter[] parameters) {
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterNames[i] = parameters[i].getName();
        }
        return parameterNames;
    }
}
