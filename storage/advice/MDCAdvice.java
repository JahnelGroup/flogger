package com.jahnelgroup.flogger.advice;

import com.jahnelgroup.flogger.annotation.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
public abstract class MDCAdvice {

    protected abstract void put(String key, String value);

    /**
     * All methods that have one parameter annotated with @MDCParam.
     */
    @Pointcut("execution(* *(.., @com.jahnelgroup.flogger.annotation.MDCParam (*), ..))")
    private void anyMethodWithMDCParams() {
    }

    /**
     * All methods that have one parameter annotated with @MDCBeanParam.
     */
    @Pointcut("execution(* *(.., @com.jahnelgroup.flogger.annotation.MDCBeanParam (*), ..))")
    private void anyMethodWithMDCBeanParams() {
    }

    /**
     * All methods that are annotated with @MDCReturn.
     */
    @Pointcut("execution(* *(..)) && @annotation(MDCReturn)")
    private void anyMethodAnnotatedWithMDCReturn(MDCReturn MDCReturn) {
    }

    /**
     * All methods that are annotated with @MDCBeanReturn.
     */
    @Pointcut("execution(* *(..)) && @annotation(MDCBeanReturn)")
    private void anyMethodAnnotatedWithMDCBeanReturn(MDCBeanReturn MDCBeanReturn) {
    }

    /**
     * addMDCParamsToMDC loops through a methods parameters and adds any annotated with @MDCParam to
     * to the MDC by calling toString on them.
     *
     * @param jp JoinPoint of the method
     */
    @Before(value = "anyMethodWithMDCParams()", argNames = "jp")
    public void addMDCParamsToMDC(JoinPoint jp) {
        Object[] args = jp.getArgs();
        Method method = getMethod(jp);
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = getParameterNames(parameters);
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(MDCParam.class)) {
                MDCParam anno = parameters[i].getAnnotation(MDCParam.class);
                // Call toString() on the parameter and add it to the MDC under the key() value or the parameter's name
                if (!anno.value().isEmpty()) {
                    registerToMDC(anno.value(), args[i].toString());
                } else {
                    registerToMDC(parameterNames[i], args[i].toString());
                }
            }
        }
    }

    /**
     * addMDCBeanParamsToMDC loops through a methods parameters and adds any annotated with @MDCBeanParam to
     * to the MDC by inspecting the type for fields/methods annotated with @MDCField/@MDCBeanField.
     *
     * @param jp JoinPoint of the method
     */
    @Before(value = "anyMethodWithMDCBeanParams()", argNames = "jp")
    public void addMDCBeanParamsToMDC(JoinPoint jp) {
        Object[] args = jp.getArgs();
        Method method = getMethod(jp);
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = getParameterNames(parameters);
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(MDCBeanParam.class)) {
                MDCBeanParam anno = parameters[i].getAnnotation(MDCBeanParam.class);
                // Expand the object for @MDCField/@MDCBeanField fields/methods and add them MDC
                if (!anno.value().isEmpty()) {
                    registerToMDC(anno.value(), args[i]);
                } else {
                    registerToMDC(parameterNames[i], args[i]);
                }
            }
        }
    }

    /**
     * addMDCReturnToMDC calls toString() on the return value of a method and adds it to the MDC.
     *
     * @param jp        JoinPoint of the method
     * @param retVal    Return value of the method
     * @param MDCReturn Annotation on the method
     */
    @AfterReturning(value = "anyMethodAnnotatedWithMDCReturn(MDCReturn)", argNames = "jp,retVal,MDCReturn", returning = "retVal")
    public void addMDCReturnToMDC(JoinPoint jp, Object retVal, MDCReturn MDCReturn) {
        MDCReturn mdcReturn = getMethod(jp).getAnnotation(MDCReturn.class);
        // Add the toString() value to the MDC
        registerToMDC(mdcReturn.value(), retVal.toString());
    }

    /**
     * addMDCBeanReturnToMDC adds the fields/methods annotated with @MDCField/@MDCBeanField, defined in
     * the return value's class, to the MDC.
     *
     * @param jp            JoinPoint of the method
     * @param retVal        Return value of the method
     * @param MDCBeanReturn Annotation on the method
     */
    @AfterReturning(value = "anyMethodAnnotatedWithMDCBeanReturn(MDCBeanReturn)", argNames = "jp,retVal,MDCBeanReturn", returning = "retVal")
    public void addMDCBeanReturnToMDC(JoinPoint jp, Object retVal, MDCBeanReturn MDCBeanReturn) {
        MDCBeanReturn mdcBeanReturn = getMethod(jp).getAnnotation(MDCBeanReturn.class);
        // Add the @MDCField/@MDCBeanField annotated fields/methods to the MDC
        registerToMDC(mdcBeanReturn.value(), retVal);
    }

    /**
     * Get a method using reflection.
     *
     * @param jp JoinPoint of the method
     * @return a reflective method
     */
    private Method getMethod(JoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        String className = methodSignature.getDeclaringTypeName();
        try {
            return Class.forName(className).getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * extractMDCData loops through the fields/methods of a class and checks for any annotated
     * with @MDCField/@MDCBeanField. It will then the found fields/methods to the MDC. If no
     * fields/methods are found, then it will add the toString() value to the MDC.
     *
     * @param key the key to add the toString() value under
     * @param arg the object to inspect using reflection
     */
    private void extractMDCData(String key, Object arg) {
        boolean addedMDCData = false;
        for (Field field : arg.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(MDCField.class)) {
                addedMDCData = true;
                registerFieldToMDC(field, arg);
            }
            if (field.isAnnotationPresent(MDCBeanField.class)) {
                addedMDCData = true;
                registerBeanFieldToMDC(field, arg);
            }
        }
        for (Method method : arg.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(MDCField.class)) {
                addedMDCData = true;
                registerMethodToMDC(method, arg);
            }
            if (method.isAnnotationPresent(MDCBeanField.class)) {
                addedMDCData = true;
                registerBeanMethodToMDC(method, arg);
            }
        }
        if (!addedMDCData) {
            put(key, arg.toString());
        }
    }

    /**
     * Register a @MDCField field to the MDC.
     *
     * @param field field to add to the MDC
     * @param arg   actual value of the field's containing object
     */
    private void registerFieldToMDC(Field field, Object arg) {
        MDCField mdcField = field.getAnnotation(MDCField.class);
        String key = field.getName();
        if (!mdcField.value().isEmpty()) {
            key = mdcField.value();
        }
        Object value = getFieldValue(field, arg);
        // Calls toString() on the value instead of adding object
        registerToMDC(key, value == null ? null : value.toString());
    }

    /**
     * Register a @MDCBeanField field to the MDC.
     *
     * @param field field to add to the MDC
     * @param arg   actual value of the field's containing object
     */
    private void registerBeanFieldToMDC(Field field, Object arg) {
        MDCBeanField mdcField = field.getAnnotation(MDCBeanField.class);
        String key = field.getName();
        if (!mdcField.value().isEmpty()) {
            key = mdcField.value();
        }
        // Adds the object to the MDC (fields/methods annotated with @MDCField/@MDCBeanField)
        registerToMDC(key, getFieldValue(field, arg));
    }

    /**
     * Register a @MDCField method's return to the MDC.
     *
     * @param method the method which we want to add the return to the MDC
     * @param arg    actual value of the method's containing object
     */
    private void registerMethodToMDC(Method method, Object arg) {
        MDCField mdcField = method.getAnnotation(MDCField.class);
        String key = method.getName();
        if (!mdcField.value().isEmpty()) {
            key = mdcField.value();
        }
        Object value = getMethodValue(method, arg);
        // Calls toString() of the value and adds that to MDC
        registerToMDC(key, value == null ? null : value.toString());
    }

    /**
     * Register a @MDCBeanField method's return to the MDC.
     *
     * @param method the method which we want to add the return to the MDC
     * @param arg    actual value of the method's containing object
     */
    private void registerBeanMethodToMDC(Method method, Object arg) {
        MDCBeanField mdcField = method.getAnnotation(MDCBeanField.class);
        String key = method.getName();
        if (!mdcField.value().isEmpty()) {
            key = mdcField.value();
        }
        // Adds the object to the MDC (fields/methods annotated with @MDCField/@MDCBeanField)
        registerToMDC(key, getMethodValue(method, arg));
    }

    /**
     * Gets the value of a field from an object using reflection.
     *
     * @param field the field we want to get
     * @param arg   the actual object we want to get the field from
     * @return the value of the field
     */
    private Object getFieldValue(Field field, Object arg) {
        Object value = null;
            // Set accessible in case private
            boolean accessibility = field.isAccessible();
            field.setAccessible(true);
        try {
            value = field.get(arg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // Don't leave it accessible
            field.setAccessible(accessibility);
        return value;
    }

    /**
     * Gets the value of a method from an object using reflection.
     *
     * @param method the method we want to get the value of
     * @param arg    the actual object we want to get the method from
     * @return the value of the method
     */
    private Object getMethodValue(Method method, Object arg) {
        Object value = null;
            // Set accessible in case private
            boolean accessibility = method.isAccessible();
            method.setAccessible(true);
        try {
            value = method.invoke(arg);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
            // Don't leave it accessible
            method.setAccessible(accessibility);

        return value;
    }

    /**
     * Inspects an object for @MDCField/@MDCBeanField annotations and adds them to the MDC.
     * <p>
     * If the object is null, adds "NULL" to the MDC.
     *
     * @param key   key to add "NULL" under
     * @param value the actual object to add to the MDC
     */
    private void registerToMDC(String key, Object value) {
        if (value != null) {
            extractMDCData(key, value);
        } else {
            put(key, "NULL");
        }
    }

    /**
     * Adds a string to the MDC.
     * <p>
     * If the string is null, adds "NULL" to the MDC.
     *
     * @param key   key to add "NULL" under
     * @param value the actual string to add to the MDC
     */
    private void registerToMDC(String key, String value) {
        if (value != null) {
            put(key, value);
        } else {
            put(key, "NULL");
        }
    }

    private String[] getParameterNames(Parameter[] params) {
        String[] parameterNames = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            parameterNames[i] = params[i].getName();
        }
        return parameterNames;
    }
}
