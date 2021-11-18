package com.fndef.plug;

import java.util.Objects;
import java.util.Set;

public interface Context {
    Object getById(String id);
    boolean contains(String id);
    Set<String> getIds();
    boolean isReady();
    boolean close();

    default  <T> T getById(String id, Class<T> type) {
        Objects.requireNonNull(type, "Type to cast is required");
        Object o = getById(id);
        if (type.isInstance(o)) {
            return type.cast(o);
        }
        throw new IllegalArgumentException("Type ["+type.getName()+"] is incompatible with with type of id ["+id+"]");
    }
}
