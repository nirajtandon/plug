package com.fndef.plug.parser.xml.resolve;

public interface ValueConvertor {
    boolean accepts(Object value);
    Object convert(Object value);

    static ValueConvertor none() {
        return new ValueConvertor() {
            @Override
            public boolean accepts(Object value) {
                return true;
            }

            @Override
            public Object convert(Object value) {
                return value;
            }
        };
    }
}
