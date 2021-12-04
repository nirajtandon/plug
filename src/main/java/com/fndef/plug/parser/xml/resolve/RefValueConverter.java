package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.Context;

public class RefValueConverter implements ValueConverter {
    private final String refId;
    private final Context context;
    public RefValueConverter(String refId, Context context) {
        this.refId = refId;
        this.context = context;
    }

    @Override
    public Object convert(Object o) {
        return context.getById(refId, ValueConverter.class).convert(o);
    }
}
