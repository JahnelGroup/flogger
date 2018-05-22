package com.jahnelgroup.flogger;

import org.aspectj.lang.JoinPoint;

public interface BindParamAspect extends BindingAspect {

    void anyMethodWithBindParams();

    void addBoundParamsToMDC(JoinPoint jp) throws FloggerException;

}
