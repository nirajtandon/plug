package com.fndef.plug.parser.xml.resolve;

public interface ValueConverter {
    Object convert(Object o);

    default <T> T convert(Object o, Class<T> type) {
        if (type.isInstance(o)) {
            return type.cast(o);
        }
        throw new IllegalArgumentException("Can't convert to type ["+type.getName()+"]");
    }
}
