package com.jahnelgroup.flogger;

public class Expanded {

    private String expandAll1 = "expanded";

    @BindFields.Exclude
    private int expandAll2 = 123;

    @BindMethods.Exclude
    private String expandAll3() {
        return "expanded3";
    }

    private int expandAll4(int expand) {
        return expand;
    }
}
