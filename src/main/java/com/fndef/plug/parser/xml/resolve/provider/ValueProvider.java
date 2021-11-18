package com.fndef.plug.parser.xml.resolve.provider;

import com.fndef.plug.parser.xml.resolve.ValueConvertor;

public class ValueProvider implements Provider {

    private final String value;
    private final ValueConvertor convertor;

    public ValueProvider(String value) {
        this(value, ValueConvertor.none());
    }

    public ValueProvider(String value, ValueConvertor convertor) {
        this.value = value;
        this.convertor = convertor;
    }

    @Override
    public Object get() {
        if (! convertor.accepts(value)) {
            throw new IllegalArgumentException("Convertor can't convert value ["+value+"]");
        }
        System.out.println("constructor value = "+convertor.convert(value));
        return convertor.convert(value);
    }
}
