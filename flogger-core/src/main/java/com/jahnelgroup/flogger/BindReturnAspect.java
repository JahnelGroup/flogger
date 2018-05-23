package com.jahnelgroup.flogger;

import org.aspectj.lang.JoinPoint;

public interface BindReturnAspect {

    void anyMethodAnnotatedWithBindReturn(BindReturn bindReturn);

    void addBoundReturnToMDC(JoinPoint jp, Object retVal, BindReturn BindReturncd ) throws FloggerException;

}
