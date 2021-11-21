package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.Context;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.TagType;
import com.fndef.plug.parser.xml.XmlConfig;
import com.fndef.plug.parser.xml.XmlConfigVisitor;
import com.fndef.plug.parser.xml.resolve.provider.InstanceMethodFactoryProvider;
import com.fndef.plug.parser.xml.resolve.provider.ObjProvider;
import com.fndef.plug.parser.xml.resolve.provider.RefProvider;
import com.fndef.plug.parser.xml.resolve.provider.StaticMethodFactoryProvider;
import com.fndef.plug.parser.xml.resolve.provider.TypeFactoryProvider;
import com.fndef.plug.parser.xml.resolve.provider.ValueProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

public class ConfigResolver implements XmlConfigVisitor {

    private final InitializingContext context;
    private final Queue<Resolvable> inprogress = new LinkedList<>();
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
            context.register(id, new FactoryMethod(id, new StaticMethodFactoryProvider(type, method)));
        } else if (hasRef.and(hasMethod).test(config) || hasFactoryRef.and(hasMethod).test(config)) {
            AttributeType attribute = hasRef.test(config) ? AttributeType.REF : AttributeType.FACTORY_REF;
            String refId = config.getAttributes().get(attribute.getAttrName());
            String method = config.getAttributes().get(AttributeType.METHOD.getAttrName());
            context.register(id, new FactoryMethod(id, new InstanceMethodFactoryProvider(refId, method, context)));
        }  else {
            String type = config.getAttributes().get(AttributeType.TYPE.getAttrName());
            List<Resolvable> constructors = drain(TagType.CONSTRUCTOR);
            context.register(id, new FactoryMethod(id, new TypeFactoryProvider(type, constructors)));
        }
    }

    @Override
    public void objectConfig(XmlConfig config) {
        final String id = config.getAttributes().get(AttributeType.ID.getAttrName());
        if (hasFactoryRef.test(config) || hasRef.test(config)) {
            final AttributeType attribute = hasRef.test(config) ? AttributeType.REF : AttributeType.FACTORY_REF;
            final String refId = config.getAttributes().get(attribute.getAttrName());
            context.register(id, new Obj(id, new RefProvider(refId, context)));
        } else if (hasType.test(config)) {
            String type = config.getAttributes().get(AttributeType.TYPE.getAttrName());
            List<Resolvable> constructors = drain(TagType.CONSTRUCTOR);
            context.register(id, new Obj(id, new ObjProvider(new TypeFactoryProvider(type, constructors))));
        } else {
            throw new IllegalArgumentException("Unsupported object arg");
        }
    }

    @Override
    public void componentConfig(XmlConfig config) {
        final String id = config.getAttributes().get(AttributeType.ID.getAttrName());
        String type = config.getAttributes().get(AttributeType.TYPE.getAttrName());
        List<Resolvable> constructors = drain(TagType.CONSTRUCTOR);
        context.register(id, new Component(id, new TypeFactoryProvider(type, constructors)));
    }

    @Override
    public void controlConfig(XmlConfig config) {
        final String id = config.getAttributes().get(AttributeType.ID.getAttrName());
        String type = config.getAttributes().get(AttributeType.TYPE.getAttrName());
        List<Resolvable> constructors = drain(TagType.CONSTRUCTOR);
        context.register(id, new Component(id, new TypeFactoryProvider(type, constructors)));
    }

    @Override
    public void constructorConfig(XmlConfig config) {
        if (hasValue.test(config)) {
            String value = config.getAttributes().get(AttributeType.VALUE.getAttrName());
            inprogress.offer(new Constructor(new ValueProvider(value)));
        } else if (hasRef.or(hasFactoryRef).test(config)) {
            AttributeType attribute = hasRef.test(config) ? AttributeType.REF : AttributeType.FACTORY_REF;
            String refId = config.getAttributes().get(attribute.getAttrName());
            inprogress.offer(new Constructor(new RefProvider(refId, context)));
        } else {
            throw new IllegalArgumentException("Unsupported constructor arg");
        }
    }

    @Override
    public void invokeConfig(XmlConfig config) {

    }

    @Override
    public void configurationConfig(XmlConfig config) {
    }

    private List<Resolvable> drain(TagType type) {
        List<Resolvable> r = new ArrayList<>();
        Iterator<Resolvable> it = new DependencyIterator(inprogress, type);
        while ((it.hasNext())) {
            r.add(it.next());
        }
        return r;
    }
}
