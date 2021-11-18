package com.fndef.plug.common;

public interface ValidationRule<T> {
    Errors validate(T config);
}
