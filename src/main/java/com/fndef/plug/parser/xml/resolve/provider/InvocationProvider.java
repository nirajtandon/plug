package com.fndef.plug.parser.xml.resolve.provider;

import com.fndef.plug.parser.xml.resolve.Invoke;
import com.fndef.plug.parser.xml.resolve.invoke.MethodInvocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvocationProvider implements Provider<List<MethodInvocation>> {
    private final List<Invoke> invokes;

    public InvocationProvider(List<Invoke> invokes) {
        Objects.requireNonNull(invokes, "Invocations are null");
        this.invokes = invokes;
    }

    public List<MethodInvocation> get() {
        List<MethodInvocation> invocations = new ArrayList<>();
        invokes.stream().forEach(i -> invocations.add(i.resolve()));
        return invocations;
    }
}
