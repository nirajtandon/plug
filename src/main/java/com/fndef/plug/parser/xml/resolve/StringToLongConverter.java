package com.fndef.plug.parser.xml.resolve;

import java.util.Objects;

public class StringToLongConverter implements ValueConverter {
    @Override
    public Long convert(Object value) {
        Objects.requireNonNull(value, "Null value");
        if (value instanceof String) {
            String v = (String) value;
            return Long.valueOf(v);
        }
        throw new IllegalArgumentException("Can't convert from type ["+value.getClass().getName()+"] to long");
    }
}
