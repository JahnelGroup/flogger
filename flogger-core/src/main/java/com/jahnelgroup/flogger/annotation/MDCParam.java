package com.jahnelgroup.flogger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method parameters annotated with @MDCParam will be added to the MDC.
 * <p>
 * The parameter's toString() method is the only thing that will be added to the MDC (as opposed to
 * inspecting methods for @MDCField/@MDCBeanField and adding those).
 *
 * @author Steven Vickers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface MDCParam {

    String value() default "";
}
