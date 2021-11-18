package com.fndef.plug.common;

public class ValidationException extends RuntimeException {
    private Errors errors;
    public ValidationException(Errors errors) {
        this(errors, null);
    }

    public ValidationException(Errors errors, Throwable th) {
        this(errors, "There are ["+(errors != null ? errors.getErrors().size() : 0)+"] errors", th);
    }

    public ValidationException(Errors errors, String message, Throwable th) {
        super(message, th);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
