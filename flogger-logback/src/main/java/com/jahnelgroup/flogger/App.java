package com.jahnelgroup.flogger;


import com.jahnelgroup.flogger.annotation.MDCParam;
import com.jahnelgroup.flogger.annotation.MDCReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public class App {

    @MDCReturn("foo() retuned")
    public static int foo() {
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("testing2");
        return 231;
    }

    public static void bar() {
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("testing4");
    }

    public static void zot(@MDCParam("param test") int test) {
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("testing6");
    }

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("testing1");
        foo();

        logger.info("testing3");
        bar();

        logger.info("testing5");
        zot(123);

        logger.info("testing7");
    }
}

