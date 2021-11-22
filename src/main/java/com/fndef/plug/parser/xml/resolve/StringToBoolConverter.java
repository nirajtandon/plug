package com.fndef.plug.parser.xml.resolve;

import java.util.Objects;

public class StringToBoolConverter implements ValueConverter {
    @Override
    public Boolean convert(Object value) {
        Objects.requireNonNull(value, "Null value");
        if (value instanceof String) {
            String v = (String) value;
            return Boolean.valueOf(v);
        }
        throw new IllegalArgumentException("Can't convert from type ["+value.getClass().getName()+"] to boolean");
    }
}
