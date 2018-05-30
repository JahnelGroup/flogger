package com.jahnelgroup.flogger;

import org.slf4j.MDC;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.jahnelgroup.flogger.utils.ReflectionUtils.*;

public abstract class BindingAspect {

    protected void put(String key, String value) {
        MDC.put(key, value);
    }

    protected void expandAndPut(Object obj) throws FloggerException {
        for (Field field : getBindableFieldsOnClass(obj.getClass())) {
            String key = field.getName();
            if (field.isAnnotationPresent(BindExpand.Include.class)) {
                BindExpand.Include include = field.getDeclaredAnnotation(BindExpand.Include.class);
                if (!include.value().isEmpty()) {
                    key = include.value();
                }
            }
            put(key, getFieldValue(field, obj).toString());
        }
        for (Method method : getBindableMethodsOnClass(obj.getClass())) {
            String key = method.getName();
            if (method.isAnnotationPresent(BindExpand.Include.class)) {
                BindExpand.Include include = method.getDeclaredAnnotation(BindExpand.Include.class);
                if (!include.value().isEmpty()) {
                    key = include.value();
                }
            }
            put(key, getMethodReturnValue(method, obj).toString());
        }
    }
}
