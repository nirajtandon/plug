package com.fndef.plug.parser;

import java.io.InputStream;

public interface ParsingStrategy<T> {
    T parse(InputStream is);
}
