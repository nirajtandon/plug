package com.fndef.plug.common;

public interface Error {
    String getErrorMessage();

    enum Severity {
        WARN,
        ERROR
    }
}
