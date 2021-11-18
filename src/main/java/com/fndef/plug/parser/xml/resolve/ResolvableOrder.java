package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;
import com.fndef.plug.parser.xml.XmlConfigVisitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.fndef.plug.parser.xml.AttributeType.FACTORY_REF;
import static com.fndef.plug.parser.xml.AttributeType.ID;
import static com.fndef.plug.parser.xml.AttributeType.REF;

public class ResolvableOrder implements XmlConfigVisitor {

    private final IdRefs resolvables = new IdRefs();
    private final Predicate<String> isRef = name ->  AttributeType.attributeType(name) == REF;
    private final Predicate<String> isFactoryRef = name ->  AttributeType.attributeType(name) == FACTORY_REF;
    private final Predicate<XmlConfig> hasReferences = c -> (c.getAttributes().containsKey(REF.getAttrName()) || c.getAttributes().containsKey(FACTORY_REF.getAttrName()));

    public Supplier<List<IdRef>> getResolveOrder() {
        return new ResolutionOrder(resolvables.get());
    }

    @Override
    public void factoryConfig(XmlConfig config) {
        resolveIdRefs(config);
    }

    @Override
    public void objectConfig(XmlConfig config) {
        resolveIdRefs(config);
    }

    @Override
    public void componentConfig(XmlConfig config) {
        resolveIdRefs(config);
    }

    @Override
    public void controlConfig(XmlConfig config) {
        resolveIdRefs(config);
    }

    @Override
    public void constructorConfig(XmlConfig config) {
        // already resolved
    }

    @Override
    public void invokeConfig(XmlConfig config) {
        // already resolved
    }

    @Override
    public void configurationConfig(XmlConfig config) {
        // no config to resolve
    }

    private void resolveIdRefs(XmlConfig config) {
        IdRef idRef = new IdRef(getAttrVal(ID, config), config.getTagType());
        List<String> refs = collectRefs(config);
        resolvables.registerId(idRef, refs);
    }

    private String getAttrVal(AttributeType attribute, XmlConfig config) {
        return config.getAttributes().get(attribute.getAttrName());
    }

    private List<String> collectRefs(XmlConfig config) {
        List<XmlConfig> children = config.getChildEntries();
        List<String> refs = new ArrayList<>();
        for (XmlConfig c : children) {
            refs.addAll(collectRefs(c));
        }

        if (hasReferences.test(config)) {
            refs.addAll(getRefs(config));
        }
        return refs;
    }

    private List<String> getRefs(XmlConfig config) {
        return config.getAttributes().keySet().stream()
                .filter(k -> isRef.or(isFactoryRef).test(k))
                .map(k -> getAttrVal(AttributeType.attributeType(k), config))
                .collect(Collectors.toList());
    }

    private static class ResolutionOrder implements Supplier<List<IdRef>> {
        private final List<IdRef> idRefs;
        private final List<IdRef> order;
        private final Set<IdRef> resolved = new HashSet<>();
        ResolutionOrder(List<IdRef> idRefs) {
            Objects.requireNonNull(idRefs, "Ids are required");
            this.idRefs = idRefs;
            this.order = new ArrayList<>(idRefs.size());
            resolve(idRefs);
        }

        private void resolve(List<IdRef> ids) {
            for (IdRef id : ids) {
                resolve(id);
            }
        }

        private void resolve(IdRef id) {
            resolved.add(id);
            List<IdRef> refs = id.getRefs();
            for (IdRef ref : refs) {
                if (!resolved.contains(ref)) {
                    resolve(ref);
                }
            }
            order.add(id);
        }

        public List<IdRef> get() {
            List<IdRef> ids = new ArrayList<>();
            for (int k = order.size() -1; k >=0; k-- ) {
                ids.add(order.get(k));
            }
            return ids;
        }
    }
}
