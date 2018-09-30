package com.di.utils;

import java.util.Comparator;

public class MyComParator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        Integer a = Integer.valueOf(s1.substring(s1.lastIndexOf("-")+1));
        Integer b = Integer.valueOf(s2.substring(s2.lastIndexOf("-")+1));
        return a>b ? 1:(a<b ? -1:0);
    }
}
