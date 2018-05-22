package com.jahnelgroup.flogger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with @MDCReturn will have their return value added to the MDC.
 * <p>
 * The return value's toString() method is the only thing that will be added to the MDC (as opposed to
 * inspecting methods for @MDCField/@MDCBeanField and adding those).
 *
 * @author Steven Vickers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MDCReturn {

    String value() default "";
}
