package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

public class ValidAttributeCheck extends XmlValidationRule {

    public ValidAttributeCheck() {
        this(new Errors());
    }

    public ValidAttributeCheck(Errors errors) {
        super(errors);
    }
    @Override
    public Errors validate(XmlConfig config) {
        config.getAttributes().keySet().stream()
                .filter(k -> ! AttributeType.isValid(k))
                .map(k -> new ValidationError(config, "Tag ["+config.getName()+"] has unknown attribute ["+k+"]"))
                .forEach(e -> getErrors().addError(e));
        return getErrors();
    }
}
