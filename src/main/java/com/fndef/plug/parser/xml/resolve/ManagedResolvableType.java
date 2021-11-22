package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;
import com.fndef.plug.parser.xml.resolve.invoke.MethodInvocation;
import com.fndef.plug.parser.xml.resolve.provider.Provider;

import java.util.List;
import java.util.function.Supplier;

public class ManagedResolvableType extends IdentifiableResolvableType {

    public ManagedResolvableType(String id, Provider<Object> provider, Supplier<TagType> tagSupplier, Provider<List<MethodInvocation>> invocationProvider) {
        super(id, provider, tagSupplier, invocationProvider);
    }
}
