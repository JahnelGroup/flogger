package com.jahnelgroup.flogger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add @MDCBeanField to a class' field/method to add the fields/methods, defined in the field's
 * class/return type class, annotated with @MDCField/@MDCBeanField to the MDC (as opposed to calling
 * toString() of the field/return type like with @MDCField).
 *
 * @author Steven Vickers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface MDCBeanField {

    String value() default "";
}
