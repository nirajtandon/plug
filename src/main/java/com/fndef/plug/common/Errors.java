package com.fndef.plug.common;

import java.util.ArrayList;
import java.util.List;

public class Errors {
    private final List<Error> errors = new ArrayList<>();
    public Error addError(Error error) {
        errors.add(error);
        return error;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<Error> getErrors() {
        return new ArrayList<>(errors);
    }

    public Errors merge(Errors other) {
        Errors merged = new Errors();
        List<Error> mergedErr = new ArrayList<>(errors);
        mergedErr.addAll(other.getErrors());
        for (Error e : mergedErr) {
            merged.addError(e);
        }
        return merged;
    }
}
