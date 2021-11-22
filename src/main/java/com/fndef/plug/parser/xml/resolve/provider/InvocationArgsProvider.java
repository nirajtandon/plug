package com.fndef.plug.parser.xml.resolve.provider;

import com.fndef.plug.parser.xml.resolve.Resolvable;
import com.fndef.plug.parser.xml.resolve.invoke.MethodInvocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvocationArgsProvider implements Provider<MethodInvocation> {

    private final MethodInvocation invocation;
    public InvocationArgsProvider(String methodName, List<Resolvable> params) {
        Objects.requireNonNull(methodName,"Method name is null");
        Objects.requireNonNull(params, "Method params are null");
        invocation = new MethodInvocation(methodName, params);
    }

    public InvocationArgsProvider(String methodName) {
        this(methodName, new ArrayList<>());
    }

    @Override
    public MethodInvocation get() {
        return invocation;
    }
}
