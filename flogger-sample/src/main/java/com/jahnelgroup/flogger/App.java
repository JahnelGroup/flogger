package com.jahnelgroup.flogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App {

    private static Logger logger = LoggerFactory.getLogger("com.jahnelgroup.flogger.App");

    @BindReturn("foo() retuned")
    public static int foo() {
        logger.info("testing2");
        return 231;
    }

    public static void bar() {
        logger.info("testing4");
    }

    public static void zot(@BindParam(value = "expandTest", expand = true) Expanded expanded) {
        logger.info("testing6");
    }

    public static void main(String[] args) {
        logger.info("testing1");
        foo();

        logger.info("testing3");
        bar();

        logger.info("testing5");
        zot(new Expanded());

        logger.info("testing7");
    }
}

