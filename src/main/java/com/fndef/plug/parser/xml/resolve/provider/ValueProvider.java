package com.fndef.plug.parser.xml.resolve.provider;

import com.fndef.plug.parser.xml.resolve.ValueConverter;

public class ValueProvider implements Provider<Object> {

    private final String value;
    private final ValueConverter convertor;

    public ValueProvider(String value, ValueConverter convertor) {
        this.value = value;
        this.convertor = convertor;
    }

    @Override
    public Object get() {
        return convertor.convert(value);
    }
}
