package com.fndef.plug.parser.xml.resolve;

import java.util.Objects;

public class StringToDoubleConverter implements ValueConverter {
    @Override
    public Double convert(Object o) {
        Objects.requireNonNull(o, "Null value");
        if (o instanceof String) {
            String v = (String) o;
            return Double.valueOf(v);
        }
        throw new IllegalArgumentException("Can't convert from type ["+o.getClass().getName()+"] to doublest");

    }
}
