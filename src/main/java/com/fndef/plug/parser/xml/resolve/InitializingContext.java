package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.common.ManagedContext;
import com.fndef.plug.Context;
import com.fndef.plug.parser.xml.TagType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InitializingContext implements Context {

    private boolean prepared;
    private boolean closed;
    private final Supplier<List<IdRef>> resolveOrder;
    private final Map<String, Resolvable> resolvables = new HashMap<>();

    private Predicate<TagType> isManaged = tag -> (tag == TagType.COMPONENT) || (tag == TagType.CONTROL);

    public InitializingContext(Supplier<List<IdRef>> resolveOrder) {
        this.resolveOrder = resolveOrder;
    }

    @Override
    public Object getById(String id) {
        return resolvableById(id);
    }

    @Override
    public boolean contains(String id) {
        ensureReady();
        return resolvables.containsKey(id);
    }

    @Override
    public Set<String> getIds() {
        ensureReady();
        return new HashSet<>(resolvables.keySet());
    }

    @Override
    public boolean isReady() {
        return prepared && !closed;
    }

    @Override
    public boolean close() {
        return (closed = true);
    }

    void register(String id, Resolvable resolvable) {
        resolvables.putIfAbsent(id, resolvable);
    }

    Context managedContext() {
        if (prepared || closed) {
            throw new IllegalStateException("Context already "+(prepared ? "initialized" : "closed"));
        }
        return prepareContext();
    }

    private void ensureReady() {
        if (!prepared) {
            throw new IllegalStateException("Context not ready");
        }

        if(closed) {
            throw new IllegalStateException("Context is closed");
        }
    }

    private Resolvable resolvableById(String id) {
        ensureReady();
        Objects.requireNonNull(id, "Id can't be null");
        return resolvables.get(id);
    }

    private Context prepareContext() {
        prepared = true;
        Map<String, Object> managedComponents = new HashMap<>();
        List<IdRef> idOrder = resolveOrder.get();
        for(IdRef id : idOrder) {
            if (isManaged.test(id.getType())) {
                Object managed = resolvables.get(id.getId()).resolve();
                managedComponents.put(id.getId(), managed);
            }
        }
        System.out.println("No of managed components ["+managedComponents.size()+"]");
        return new ManagedContext(managedComponents);
    }
}
