package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.XmlConfig;

import java.util.function.Predicate;

public class NoAttributesCheck extends XmlValidationRule {

    private final Predicate<XmlConfig> noAttributes = cs -> cs.getAttributes().isEmpty();

    public NoAttributesCheck() {
        this(new Errors());
    }

    public NoAttributesCheck(Errors errors) {
        super(errors);
    }

    @Override
    public Errors validate(XmlConfig config) {
        if (noAttributes.negate().test(config)) {
            getErrors().addError(new ValidationError(config, "Tag ["+config.getName()+"] doesn't support attributes"));
        }
        return getErrors();
    }
}
