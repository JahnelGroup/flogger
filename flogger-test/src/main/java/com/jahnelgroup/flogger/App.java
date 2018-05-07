package com.jahnelgroup.flogger;


import com.jahnelgroup.flogger.annotation.MDCParam;
import com.jahnelgroup.flogger.annotation.MDCReturn;

public class App {

    @MDCReturn
    public static int foo() {
        return 231;
    }

    public static void bar() {}

    public static void zot(@MDCParam(value = "test") int test) {}

    public static void main(String[] args) {
        foo();
        bar();
        zot(123);
    }
}

