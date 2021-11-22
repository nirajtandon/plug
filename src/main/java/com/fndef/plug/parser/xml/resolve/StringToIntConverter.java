package com.fndef.plug.parser.xml.resolve;

import java.util.Objects;

public class StringToIntConverter implements ValueConverter {
    @Override
    public Integer convert(Object value) {
        Objects.requireNonNull(value, "Null value");
        if (value instanceof String) {
            String v = (String) value;
            return Integer.valueOf(v);
        }
        throw new IllegalArgumentException("Can't convert from type ["+value.getClass().getName()+"] to int");
    }
}
