package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

public class RequiredAttributeCheck extends XmlValidationRule {
    private final AttributeType requiredAttribute;

    public RequiredAttributeCheck(AttributeType requiredAttribute) {
        this(requiredAttribute, new Errors());
    }

    public RequiredAttributeCheck(AttributeType requiredAttribute, Errors errors) {
        super(errors);
        this.requiredAttribute = requiredAttribute;
    }

    @Override
    public Errors validate(XmlConfig config) {
        if (! config.getAttributes().containsKey(requiredAttribute.getAttrName())) {
            getErrors().addError(new ValidationError(config, "Tag ["+config.getName()+"] requires attribute ["+requiredAttribute.getAttrName()+"]"));
        }
        return getErrors();
    }
}
