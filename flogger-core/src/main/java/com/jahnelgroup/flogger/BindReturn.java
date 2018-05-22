package com.jahnelgroup.flogger;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface BindReturn {

    String value() default "";

}
