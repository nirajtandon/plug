package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.common.ValidationRule;
import com.fndef.plug.parser.xml.XmlConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ValidationAggregate implements ValidationRule<XmlConfig> {
    private final Errors errors;
    private final List<ValidationRule> validators = new ArrayList<>();

    ValidationAggregate(Errors errors) {
        this.errors = errors;
    }

    ValidationAggregate() {
        this(new Errors());
    }

    public static ValidationAggregate of(ValidationRule<XmlConfig>... validators) {
        Objects.requireNonNull(validators, "No validator specified");

        ValidationAggregate aggregate = new ValidationAggregate();
        for (ValidationRule cv : validators) {
            aggregate.nextValidation(cv);
        }
        return aggregate;
    }

    public static ValidationAggregate with(Errors errors, ValidationRule... validators) {
        Objects.requireNonNull(errors, "Error container can't be missing");
        Objects.requireNonNull(validators, "No validator specified");

        ValidationAggregate aggregate = new ValidationAggregate((errors));
        for (ValidationRule cv : validators) {
            aggregate.nextValidation(cv);
        }
        return aggregate;
    }

    public ValidationAggregate nextValidation(ValidationRule validator) {
        Objects.requireNonNull(validator, "Missing validator");
        validators.add(validator);
        return this;
    }

    @Override
    public Errors validate(XmlConfig config) {
        validators.stream()
                .map(v -> v.validate(config))
                .flatMap(validationErrors -> validationErrors.getErrors().stream())
                .forEach(e -> errors.addError(e));
        return errors;
    }

    public Errors getErrors() {
        return errors;
    }

}
