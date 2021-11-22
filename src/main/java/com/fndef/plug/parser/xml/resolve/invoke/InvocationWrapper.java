package com.fndef.plug.parser.xml.resolve.invoke;

import com.fndef.plug.parser.xml.resolve.provider.Provider;

import java.util.List;
import java.util.Objects;

public class InvocationWrapper {

    private final Object target;
    private final Provider<List<MethodInvocation>> invocables;

    public InvocationWrapper(Object target, Provider<List<MethodInvocation>> invocations) {
        Objects.requireNonNull(target, "Method invocation target object is null");
        Objects.requireNonNull(invocations, "Method invocations is null");
        this.target = target;
        this.invocables = invocations;
    }

    public void invoke() {
        final List<MethodInvocation> invocations = invocables.get();
        invocations.stream().forEach(mi -> mi.invoke(target));
    }

}
