package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;

import java.util.ArrayList;
import java.util.List;

class IdRef {
    private final String id;
    private final TagType type;
    private final List<IdRef> refs = new ArrayList<>();

    IdRef(String id, TagType type) {
        this.id = id;
        this.type = type;
    }

    void addRef(IdRef entry) {
        refs.add(entry);
    }

    List<IdRef> getRefs() {
        return new ArrayList<>(refs);
    }

    public String getId() {
        return id;
    }

    public TagType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IdRef) {
            IdRef other = (IdRef) o;
            return getId().equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "IdRef ["+id+"] and ref count ["+refs.size()+"]";
    }
}
