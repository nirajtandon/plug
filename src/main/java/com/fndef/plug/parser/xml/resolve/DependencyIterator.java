package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

public class DependencyIterator implements Iterator<Resolvable> {

    private final TagType dependencyType;
    private final Deque<Resolvable> source;

    DependencyIterator(Deque<Resolvable> queue, TagType dependencyType) {
        Objects.requireNonNull(queue, "Missing iterable");
        Objects.requireNonNull(dependencyType, "Type of elements is required");
        this.dependencyType = dependencyType;
        source = queue;
    }

    @Override
    public boolean hasNext() {
        return Optional.ofNullable(source.peekFirst()).map(r -> r.type() == dependencyType).orElse(false);
    }

    @Override
    public Resolvable next() {
        return Optional.ofNullable(source.removeFirst())
                .filter(r -> r.type() == dependencyType)
                .orElseThrow(() -> new NoSuchElementException("No more elements of type ["+dependencyType.getName()+"]"));
    }
}
