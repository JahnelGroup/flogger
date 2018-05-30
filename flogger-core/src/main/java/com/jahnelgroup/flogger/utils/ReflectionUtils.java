package com.jahnelgroup.flogger.utils;

import com.jahnelgroup.flogger.BindExpand;
import com.jahnelgroup.flogger.FloggerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ReflectionUtils {

    private static final String ILLEGAL_ACCESS_EXCEPTION_MSG = "Error accessing %s on class: %s";
    private static final String CLASS_NOT_FOUND_EXCEPTION_MSG = "Error finding class: %s";
    private static final String NO_SUCH_METHOD_EXCEPTION_MSG = "Error finding %s(%s) on class: %s";
    private static final String INVOCATION_TARGET_EXCEPTION_MSG = "%s.%s threw an exception";

    public static Method getMethodFromJointPoint(JoinPoint joinPoint) throws FloggerException {
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

    public static Stream<Field> getFieldsOnClass(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields());
    }

    public static Stream<Method> getMethodsOnClass(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods());
    }

    public static Stream<Method> getMethodsWithoutParametersOnClass(Class<?> clazz) {
        return getMethodsOnClass(clazz).filter(method -> method.getParameterCount() == 0);
    }

    public static Stream<Field> getIncludedBindableFieldsOnClass(Class<?> clazz) {
        Stream<Field> fields = getFieldsOnClass(clazz);
        return fields.filter(field -> field.isAnnotationPresent(BindExpand.Include.class));
    }

    public static Stream<Field> filterOutExcludedFields(Stream<Field> fields) {
        return fields.filter(field -> !field.isAnnotationPresent(BindExpand.Exclude.class));
    }

    public static List<Field> getBindableFieldsOnClass(Class<?> clazz) {
        if (clazz.isAnnotationPresent(BindExpand.class)) {
            BindExpand bindExpand = clazz.getDeclaredAnnotation(BindExpand.class);
            if (bindExpand.onlyExplicityIncluded()) {
                return getIncludedBindableFieldsOnClass(clazz).collect(Collectors.toList());
            }
        }
        return filterOutExcludedFields(getFieldsOnClass(clazz)).collect(Collectors.toList());
    }

    public static Stream<Method> getIncludedBindableMethodsOnClass(Class<?> clazz) {
        Stream<Method> methods = getMethodsWithoutParametersOnClass(clazz);
        return methods.filter(method -> method.isAnnotationPresent(BindExpand.Include.class));
    }

    public static Stream<Method> filterOutExcludedMethods(Stream<Method> methods) {
        return methods.filter(method -> !method.isAnnotationPresent(BindExpand.Exclude.class));
    }

    public static List<Method> getBindableMethodsOnClass(Class<?> clazz) {
        if (clazz.isAnnotationPresent(BindExpand.class)) {
            BindExpand bindExpand = clazz.getDeclaredAnnotation(BindExpand.class);
            if (bindExpand.onlyExplicityIncluded()) {
                return getIncludedBindableMethodsOnClass(clazz).collect(Collectors.toList());
            }
        }
        return filterOutExcludedMethods(getMethodsWithoutParametersOnClass(clazz)).collect(Collectors.toList());
    }

    public static Object getFieldValue(Field field, Object obj) throws FloggerException {
        boolean isAccessible = field.isAccessible();
        if (!isAccessible) {
            field.setAccessible(true);
        }
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new FloggerException(String.format(ILLEGAL_ACCESS_EXCEPTION_MSG, field.getName(), obj.getClass()), e);
        } finally {
            field.setAccessible(isAccessible);
        }
    }

    public static Object getMethodReturnValue(Method method, Object obj) throws FloggerException {
        boolean isAccessible = method.isAccessible();
        if (!isAccessible) {
            method.setAccessible(true);
        }
        try {
            return method.invoke(obj);
        } catch (IllegalAccessException e) {
            throw new FloggerException(String.format(ILLEGAL_ACCESS_EXCEPTION_MSG, method.getName(), obj.getClass()), e);
        } catch (InvocationTargetException e) {
            throw new FloggerException(String.format(INVOCATION_TARGET_EXCEPTION_MSG, obj.getClass(), method.getName()), e);
        } finally {
            method.setAccessible(isAccessible);
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
