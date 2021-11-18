package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.resolve.provider.Provider;

public class Obj implements Resolvable {

    private final String id;
    private final Provider provider;

    public Obj(String id, Provider provider) {
        this.id = id;
        this.provider = provider;
    }

    @Override
    public Object resolve() {
        return provider.get();
    }

    public String toString() {
        return "Object [id="+id+"]";
    }
}
