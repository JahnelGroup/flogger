package com.jahnelgroup.flogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BindingAspect {

    private static final String ILLEGAL_ACCESS_EXCEPTION_MSG = "Error accessing %s on class: %s";
    private static final String INVOCATION_TARGET_EXCEPTION_MSG = "%s.%s threw an exception";

    protected void put(String key, String value) {
        MDC.put(key, value);
    }

    protected void expandAndPut(Object arg) throws FloggerException {
        for (Field field : arg.getClass().getDeclaredFields()) {
            boolean isFieldAccessible = field.isAccessible();
            if (!isFieldAccessible) {
                field.setAccessible(true);
            }
            try {
                put(field.getName(), field.get(arg).toString());
            } catch (IllegalAccessException e) {
                throw new FloggerException(String.format(ILLEGAL_ACCESS_EXCEPTION_MSG, field.getName(), arg.getClass()), e);
            } finally {
                field.setAccessible(isFieldAccessible);
            }
        }
        for (Method method : arg.getClass().getDeclaredMethods()) {
            if (method.getParameterTypes().length == 0) {
                boolean isMethodAccessible = method.isAccessible();
                if (!isMethodAccessible) {
                    method.setAccessible(true);
                }
                try {
                    put(method.getName(), method.invoke(arg).toString());
                } catch (IllegalAccessException e) {
                    throw new FloggerException(String.format(ILLEGAL_ACCESS_EXCEPTION_MSG, method.getName(), arg.getClass()), e);
                } catch (InvocationTargetException e) {
                    throw new FloggerException(String.format(INVOCATION_TARGET_EXCEPTION_MSG, arg.getClass(), method.getName()), e);
                } finally {
                    method.setAccessible(isMethodAccessible);
                }
            }
        }
    }
}
