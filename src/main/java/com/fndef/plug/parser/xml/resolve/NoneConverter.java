package com.fndef.plug.parser.xml.resolve;

public class NoneConverter implements ValueConverter {
    @Override
    public Object convert(Object value) {
        return value;
    }
}
