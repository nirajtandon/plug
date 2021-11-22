package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;
import com.fndef.plug.parser.xml.resolve.provider.Provider;

import java.util.Objects;
import java.util.function.Supplier;

public class ResolvableType implements Resolvable<Object> {

    private final Provider<Object> provider;
    private final Supplier<TagType> typeSupplier;

    public ResolvableType(Provider<Object> provider, Supplier<TagType> tagTypeSupplier) {
        Objects.requireNonNull(provider, "Resolvable provider is null");
        Objects.requireNonNull("Tag type supplier is null");
        this.provider = provider;
        this.typeSupplier = tagTypeSupplier;
    }

    @Override
    public Object resolve() {
        return provider.get();
    }

    @Override
    public TagType type() {
        return typeSupplier.get();
    }

    public Provider<Object> getProvider() {
        return provider;
    }
}
