package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

public class TagSupportsAttributeCheck extends XmlValidationRule {

    public TagSupportsAttributeCheck() {
        this(new Errors());
    }

    public TagSupportsAttributeCheck(Errors errors) {
        super(errors);
    }

    @Override
    public Errors validate(XmlConfig config) {
        config.getAttributes().keySet().stream()
                .filter(k -> AttributeType.isValid(k))
                .filter(k -> ! config.getTagType().supportedAttributes().contains(AttributeType.attributeType(k)))
                .map(k -> new ValidationError(config, "Tag ["+config.getName()+"] does not support attribute ["+k+"]"))
                .forEach(e -> getErrors().addError(e));

        return getErrors();
    }
}
