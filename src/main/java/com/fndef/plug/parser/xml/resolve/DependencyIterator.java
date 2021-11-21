package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

public class DependencyIterator implements Iterator<Resolvable> {

    private final TagType dependencyType;
    private final Queue<Resolvable> source;

    DependencyIterator(Queue<Resolvable> queue, TagType dependencyType) {
        Objects.requireNonNull(queue, "Missing iterable");
        Objects.requireNonNull(dependencyType, "Type of elements is required");
        this.dependencyType = dependencyType;
        source = queue;
    }

    @Override
    public boolean hasNext() {
        return Optional.ofNullable(source.peek()).map(r -> r.type() == dependencyType).orElse(false);
    }

    @Override
    public Resolvable next() {
        return Optional.ofNullable(source.poll())
                .filter(r -> r.type() == dependencyType)
                .orElseThrow(() -> new NoSuchElementException("No more elements of type ["+dependencyType.getName()+"]"));
    }
}
