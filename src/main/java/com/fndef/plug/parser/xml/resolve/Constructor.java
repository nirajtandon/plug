package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.resolve.provider.Provider;

public class Constructor implements Resolvable {

    private final Provider provider;
    public Constructor(Provider provider) {
        this.provider = provider;
    }
    @Override
    public Object resolve() {
        return provider.get();
    }
}
