package com.fndef.plug.common;

import com.fndef.plug.Context;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ManagedContext implements Context {

    private final Map<String, Object> managedComponents;

    public ManagedContext(Map<String, Object> managedComponents) {
        this.managedComponents = managedComponents;
    }

    @Override
    public Object getById(String id) {
        return getComponent(id);
    }

    @Override
    public boolean contains(String id) {
        Objects.requireNonNull(id, "Id can't be null");
        return managedComponents.containsKey(id);
    }

    @Override
    public Set<String> getIds() {
        return new HashSet<>(managedComponents.keySet());
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public boolean close() {
        return false;
    }

    private Object getComponent(String id) {
        Objects.requireNonNull(id, "Id can'tbe null");

        if (!managedComponents.containsKey(id)) {
            throw new IllegalArgumentException("Id [" + id + "] not found");
        }
        return managedComponents.get(id);
    }
}
