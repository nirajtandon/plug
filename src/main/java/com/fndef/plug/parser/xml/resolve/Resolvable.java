package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;

public interface Resolvable<T> {
    T resolve();
    TagType type();
}
