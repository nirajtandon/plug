package com.fndef.plug;

public class Test {
    private String s;
    private int i;
    public Test(int i, String s) {
        this.s = s;
        this.i = i;
    }

    public void init() {
        System.out.println("init works");
    }

    public void init(String msg1, String msg2) {
        System.out.println("init works : msg1 : "+msg1+", msg2 : "+msg2);
    }

    public String get() {
        return s;
    }

    public String toString() {
        return "test ["+s+", i="+i+"]";
    }
}
