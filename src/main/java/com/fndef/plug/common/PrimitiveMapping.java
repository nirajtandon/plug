package com.fndef.plug.common;

public enum PrimitiveMapping {
    BYTE(byte.class, Byte.class),
    SHORT(short.class, Short.class),
    INT(int.class, Integer.class),
    LONG(long.class, Long.class),
    DOUBLE(double.class, Double.class),
    FLOAT(float.class, Float.class),
    BOOL(boolean.class, Boolean.class),
    CHAR(char.class, Character.class);

    private Class from;
    private Class to;
    PrimitiveMapping(Class from, Class to) {
        this.from = from;
        this.to = to;
    }

    public static boolean isAssignable(Class from, Class to) {
        for(PrimitiveMapping m : values()) {
            if ((from == m.from || from == m.to) && (to == m.from || to == m.to)) {
                return true;
            }
        }
        return false;
    }
}
