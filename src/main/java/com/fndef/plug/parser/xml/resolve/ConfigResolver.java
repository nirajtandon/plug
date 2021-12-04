package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.Context;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.TagType;
import com.fndef.plug.parser.xml.XmlConfig;
import com.fndef.plug.parser.xml.XmlConfigVisitor;
import com.fndef.plug.parser.xml.resolve.provider.InstanceMethodFactoryProvider;
import com.fndef.plug.parser.xml.resolve.provider.InvocationArgsProvider;
import com.fndef.plug.parser.xml.resolve.provider.InvocationProvider;
import com.fndef.plug.parser.xml.resolve.provider.ObjProvider;
import com.fndef.plug.parser.xml.resolve.provider.RefProvider;
import com.fndef.plug.parser.xml.resolve.provider.StaticMethodFactoryProvider;
import com.fndef.plug.parser.xml.resolve.provider.TypeFactoryProvider;
import com.fndef.plug.parser.xml.resolve.provider.ValueProvider;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ConfigResolver implements XmlConfigVisitor {

    private final InitializingContext context;
    private final Deque<Resolvable> inprogress = new ArrayDeque<>();
    private final Predicate<XmlConfig> hasType = config -> config.getAttributes().containsKey(AttributeType.TYPE.getAttrName());
    private final Predicate<XmlConfig> hasMethod = config -> config.getAttributes().containsKey(AttributeType.METHOD.getAttrName());
    private final Predicate<XmlConfig> hasRef = config -> config.getAttributes().containsKey(AttributeType.REF.getAttrName());
    private final Predicate<XmlConfig> hasFactoryRef = config -> config.getAttributes().containsKey(AttributeType.FACTORY_REF.getAttrName());
    private final Predicate<XmlConfig> hasValue = config -> config.getAttributes().containsKey(AttributeType.VALUE.getAttrName());

    public ConfigResolver(InitializingContext context) {
        this.context = context;
    }

    public Context getContext() {
        return context.managedContext();
    }

    @Override
    public void factoryConfig(XmlConfig config) {
        final String id = config.getAttributes().get(AttributeType.ID.getAttrName());
        if (hasType.and(hasMethod).test(config)) {
            String type = config.getAttributes().get(AttributeType.TYPE.getAttrName());
            String method = config.getAttributes().get(AttributeType.METHOD.getAttrName());
            context.register(id, new IdentifiableResolvableType(id, new StaticMethodFactoryProvider(type, method),  () -> TagType.FACTORY, new InvocationProvider(new ArrayList<>())));
        } else if (hasRef.and(hasMethod).test(config) || hasFactoryRef.and(hasMethod).test(config)) {
            AttributeType attribute = hasRef.test(config) ? AttributeType.REF : AttributeType.FACTORY_REF;
            String refId = config.getAttributes().get(attribute.getAttrName());
            String method = config.getAttributes().get(AttributeType.METHOD.getAttrName());
            context.register(id, new IdentifiableResolvableType(id, new InstanceMethodFactoryProvider(refId, method, context), () -> TagType.FACTORY, new InvocationProvider(new ArrayList<>())));
        }  else {
            String type = config.getAttributes().get(AttributeType.TYPE.getAttrName());
            List<Invoke> invocations = drain(TagType.INVOKE, Invoke.class);
            List<Resolvable> constructors = drain(TagType.CONSTRUCTOR);
            context.register(id, new IdentifiableResolvableType(id, new TypeFactoryProvider(type, constructors), () -> TagType.FACTORY, new InvocationProvider(invocations)));
        }
    }

    @Override
    public void objectConfig(XmlConfig config) {
        final String id = config.getAttributes().get(AttributeType.ID.getAttrName());
        if (hasFactoryRef.test(config) || hasRef.test(config)) {
            final AttributeType attribute = hasRef.test(config) ? AttributeType.REF : AttributeType.FACTORY_REF;
            final String refId = config.getAttributes().get(attribute.getAttrName());
            context.register(id, new IdentifiableResolvableType(id, new RefProvider(refId, context), () -> TagType.OBJECT, new InvocationProvider(new ArrayList<>())));
        } else if (hasType.test(config)) {
            String type = config.getAttributes().get(AttributeType.TYPE.getAttrName());
            List<Invoke> invocations = drain(TagType.INVOKE, Invoke.class);
            List<Resolvable> constructors = drain(TagType.CONSTRUCTOR);
            context.register(id, new IdentifiableResolvableType(id, new ObjProvider(new TypeFactoryProvider(type, constructors)), () -> TagType.OBJECT, new InvocationProvider(invocations)));
        } else {
            throw new IllegalArgumentException("Unsupported object arg");
        }
    }

    @Override
    public void componentConfig(XmlConfig config) {
        final String id = config.getAttributes().get(AttributeType.ID.getAttrName());
        String type = config.getAttributes().get(AttributeType.TYPE.getAttrName());
        List<Invoke> invocations = drain(TagType.INVOKE, Invoke.class);
        List<Resolvable> constructors = drain(TagType.CONSTRUCTOR);
        context.register(id, new ManagedResolvableType(id, new TypeFactoryProvider(type, constructors), () -> TagType.COMPONENT, new InvocationProvider(invocations)));
    }

    @Override
    public void controlConfig(XmlConfig config) {
        final String id = config.getAttributes().get(AttributeType.ID.getAttrName());
        String type = config.getAttributes().get(AttributeType.TYPE.getAttrName());
        List<Invoke> invocations = drain(TagType.INVOKE, Invoke.class);
        List<Resolvable> constructors = drain(TagType.CONSTRUCTOR);
        context.register(id, new ManagedResolvableType(id, new TypeFactoryProvider(type, constructors), () -> TagType.CONTROL, new InvocationProvider(invocations)));
    }

    @Override
    public void constructorConfig(XmlConfig config) {
        if (hasValue.test(config)) {
            String value = config.getAttributes().get(AttributeType.VALUE.getAttrName());
            ValueConverter converter = valueConverter(config);
            inprogress.addFirst(new ResolvableType(new ValueProvider(value, converter),() -> TagType.CONSTRUCTOR));
        } else if (hasRef.or(hasFactoryRef).test(config)) {
            AttributeType attribute = hasRef.test(config) ? AttributeType.REF : AttributeType.FACTORY_REF;
            String refId = config.getAttributes().get(attribute.getAttrName());
            inprogress.addFirst(new ResolvableType(new RefProvider(refId, context), () -> TagType.CONSTRUCTOR));
        } else {
            throw new IllegalArgumentException("Unsupported constructor arg");
        }
    }

    @Override
    public void invokeConfig(XmlConfig config) {
        String methodName = config.getAttributes().get(AttributeType.METHOD.getAttrName());
        List<Resolvable> params = drain(TagType.PARAM);
        inprogress.addFirst(new Invoke(new InvocationArgsProvider(methodName, params)));
    }

    @Override
    public void configurationConfig(XmlConfig config) {
    }

    @Override
    public void paramConfig(XmlConfig config) {
        // if convertable then supply predefined or custom convertor from config
        if (hasValue.test(config)) {
            String val = config.getAttributes().get(AttributeType.VALUE.getAttrName());
            ValueConverter converter = valueConverter(config);
            inprogress.addFirst(new ResolvableType(new ValueProvider(val, converter), () -> TagType.PARAM));
        } else if (hasRef.or(hasFactoryRef).test(config)) {
            AttributeType attribute = hasRef.test(config) ? AttributeType.REF : AttributeType.FACTORY_REF;
            String refId = config.getAttributes().get(attribute.getAttrName());
            inprogress.addFirst(new ResolvableType(new RefProvider(refId, context),() -> TagType.PARAM));
        } else {
            throw new IllegalArgumentException("Invalid param - no value provider");
        }
    }

    private ValueConverter valueConverter(XmlConfig config) {
        String converter = config.getAttributes().get(AttributeType.CONVERTER.getAttrName());
        return Optional.ofNullable(converter)
                .map(c -> ValueConversion.getConverter(c, context))
                .orElse(ValueConversion.ProvidedConversionType.NONE.getConverter());
    }

    private List<Resolvable> drain(TagType type) {
        Deque<Resolvable> r = new ArrayDeque<>();
        Iterator<Resolvable> it = new DependencyIterator(inprogress, type);
        while ((it.hasNext())) {
            r.addFirst(it.next());
        }
        return new ArrayList<>(r);
    }

    private <T extends Resolvable> List<T> drain(TagType type, Class<T> clazz) {
        List<Resolvable> resolvables = drain(type);
        List<T> rType = new ArrayList<>();
        for (Resolvable r : resolvables) {
            if (!clazz.isInstance(r)) {
                throw new IllegalArgumentException("Invalid resolvable type");
            }
            rType.add(clazz.cast(r));
        }
        return rType;
    }
}
