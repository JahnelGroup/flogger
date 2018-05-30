package com.jahnelgroup.flogger;

@BindExpand(onlyExplicityIncluded = true)
public class ExpandedAll {

    @BindExpand.Include("expandNameTest")
    private String expandMe = "expanded";
    @BindExpand.Include
    private int expandMe2 = 123;

    private String expandMe3() {
        return "expanded3";
    }

    private int expandMe4(int expand) {
        return expand;
    }
}
