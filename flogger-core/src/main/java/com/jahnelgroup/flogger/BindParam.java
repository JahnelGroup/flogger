package com.jahnelgroup.flogger;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Inherited
public @interface BindParam {

    String value() default "";

}
