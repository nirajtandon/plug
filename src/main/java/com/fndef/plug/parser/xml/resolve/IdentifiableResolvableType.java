package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;
import com.fndef.plug.parser.xml.resolve.invoke.InvocationWrapper;
import com.fndef.plug.parser.xml.resolve.invoke.MethodInvocation;
import com.fndef.plug.parser.xml.resolve.provider.Provider;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class IdentifiableResolvableType extends ResolvableType {
    private final String id;
    private final Provider<List<MethodInvocation>> invocationProvider;
    public IdentifiableResolvableType(String id, Provider<Object> provider, Supplier<TagType> tagTypeSupplier, Provider<List<MethodInvocation>> invocationProvider) {
        super(provider, tagTypeSupplier);
        Objects.requireNonNull(id, "Id is null");
        this.id = id;
        this.invocationProvider = invocationProvider;
    }

    @Override
    public Object resolve() {
        try {
            Object target = getProvider().get();
            // invocationProvider.map(p -> new InvocationWrapper(target, p)).ifPresent(mi -> mi.invoke());
            InvocationWrapper methodInvocations = new InvocationWrapper(target, invocationProvider);
            methodInvocations.invoke();
            return target;
        } catch (RuntimeException rte) {
            throw new RuntimeException("Resolution for id ["+getId()+"] failed", rte);
        }
    }
    public String getId() {
        return id;
    }
}
