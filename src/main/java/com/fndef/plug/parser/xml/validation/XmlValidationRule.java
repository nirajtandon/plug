package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.common.ValidationRule;
import com.fndef.plug.parser.xml.XmlConfig;

public abstract class XmlValidationRule implements ValidationRule<XmlConfig> {
    private final Errors errors;

    public XmlValidationRule(Errors errors) {
        this.errors = errors;
    }

    protected Errors getErrors() {
        return errors;
    }

    public abstract Errors validate(XmlConfig config);
}
