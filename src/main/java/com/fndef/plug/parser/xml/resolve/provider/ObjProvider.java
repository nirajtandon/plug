package com.fndef.plug.parser.xml.resolve.provider;

public class ObjProvider implements Provider<Object> {
    private final Provider provider;
    private Object value;
    public ObjProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public Object get() {
        if (value == null) {
            value = provider.get();
        }
        return value;
    }
}
