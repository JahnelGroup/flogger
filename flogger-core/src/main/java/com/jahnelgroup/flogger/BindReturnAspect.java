package com.jahnelgroup.flogger;

import org.aspectj.lang.JoinPoint;

public interface BindReturnAspect extends BindingAspect {

    void anyMethodAnnotatedWithBindReturn(BindReturn bindReturn);

    void addBoundReturnToMDC(JoinPoint jp, Object retVal, BindReturn bindReturn);

}
