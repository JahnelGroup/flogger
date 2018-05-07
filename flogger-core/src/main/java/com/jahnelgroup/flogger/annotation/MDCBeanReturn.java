package com.jahnelgroup.flogger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with @MDCBeanReturn will have their return value added to the MDC.
 * <p>
 * The class of the return value will be inspected for @MDCField/@MDCBeanField annotated fields/methods
 * and only add those values to the MDC (as opposed to adding the return value's toString() method
 * with @MDCReturn).
 *
 * @author Steven Vickers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MDCBeanReturn {

    String value() default "";
}
