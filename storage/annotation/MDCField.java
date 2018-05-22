package com.jahnelgroup.flogger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add @MDCField to a class' field/method to add the toString() value of the annotated field/method
 * to the MDC when that class is being inspected using @MDCBeanReturn/@MDCBeanParam/@MDCBeanField.
 *
 * @author Steven Vickers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface MDCField {

    String value() default "";
}
