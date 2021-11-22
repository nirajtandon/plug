package com.fndef.plug.parser.xml.resolve.provider;

import com.fndef.plug.Context;
import com.fndef.plug.parser.xml.resolve.Resolvable;

public class RefProvider implements Provider<Object> {

    private final String id;
    private final Context context;

    public RefProvider(String id, Context context) {
        this.id = id;
        this.context = context;
    }

    @Override
    public Object get() {
        return context.getById(id, Resolvable.class).resolve();
    }
}
