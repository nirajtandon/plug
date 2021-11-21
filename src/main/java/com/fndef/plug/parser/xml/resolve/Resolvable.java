package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.parser.xml.TagType;

public interface Resolvable {
    Object resolve();
    TagType type();
}
