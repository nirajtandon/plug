package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;
import com.fndef.plug.parser.xml.resolve.provider.Provider;

public class FactoryMethod implements Resolvable {
    private final String id;
    private final Provider provider;
    public FactoryMethod(String id, Provider provider) {
        this.id = id;
        this.provider = provider;
    }
    @Override
    public Object resolve() {
        return provider.get();
    }

    @Override
    public TagType type() {
        return TagType.FACTORY;
    }

    public String toString() {
        return "Factory method [id="+id+"]";
    }
}
