package com.jahnelgroup.flogger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method parameters annotated with @MDCBeanParam will be added to the MDC.
 * <p>
 * The class of the parameter will be inspected for @MDCField/@MDCBeanField annotated fields/methods
 * and only add those values to the MDC (as opposed to adding the parameter's toString() method
 * with @MDCParam).
 *
 * @author Steven Vickers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface MDCBeanParam {

    String value() default "";
}
