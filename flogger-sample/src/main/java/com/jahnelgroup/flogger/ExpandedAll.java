package com.jahnelgroup.flogger;

@BindFields(onlyExplicityIncluded = true)
@BindMethods(onlyExplicityIncluded = true)
public class ExpandedAll {

    @BindFields.Include("expandNameTest")
    private String expandMe = "expanded";
    @BindFields.Include
    private int expandMe2 = 123;

    private String expandMe3() {
        return "expanded3";
    }

    private int expandMe4(int expand) {
        return expand;
    }
}
