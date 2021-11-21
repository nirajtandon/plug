package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;
import com.fndef.plug.parser.xml.resolve.provider.Provider;

public class Control implements Resolvable {
    private final String id;
    private final Provider provider;

    public Control(String id, Provider provider) {
        this.id = id;
        this.provider = provider;
    }

    @Override
    public Object resolve() {
        return provider.get();
    }

    @Override
    public TagType type() {
        return TagType.CONTROL;
    }
}
