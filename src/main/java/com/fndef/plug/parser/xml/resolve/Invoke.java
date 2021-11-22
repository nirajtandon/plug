package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;
import com.fndef.plug.parser.xml.resolve.invoke.MethodInvocation;
import com.fndef.plug.parser.xml.resolve.provider.Provider;

public class Invoke implements Resolvable<MethodInvocation> {

    private final Provider<MethodInvocation> provider;

    public Invoke(Provider provider) {
        this.provider = provider;
    }

    @Override
    public MethodInvocation resolve() {
        return provider.get();
    }

    @Override
    public TagType type() {
        return TagType.INVOKE;
    }
}
