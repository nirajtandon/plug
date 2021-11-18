package com.fndef.plug.parser;

import com.fndef.plug.Context;

public interface ResolveStrategy<T> {
    Context resolve(T unresolvedConfig);
}
