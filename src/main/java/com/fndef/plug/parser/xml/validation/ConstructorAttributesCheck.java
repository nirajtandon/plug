package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

import java.util.function.Predicate;

public class ConstructorAttributesCheck extends XmlValidationRule {

    private final Predicate<XmlConfig> hasValueAttr = config -> config.getAttributes().containsKey(AttributeType.VALUE.getAttrName());
    private final Predicate<XmlConfig> hasRefAttr = config -> config.getAttributes().containsKey(AttributeType.REF.getAttrName());
    private final Predicate<XmlConfig> hasFactoryRef = config -> config.getAttributes().containsKey(AttributeType.FACTORY_REF.getAttrName());

    public ConstructorAttributesCheck() {
        this(new Errors());
    }

    public ConstructorAttributesCheck(Errors errors) {
        super(errors);
    }

    @Override
    public Errors validate(XmlConfig config) {
        if (! hasValueAttr.or(hasRefAttr).or(hasFactoryRef).test(config)) {
            getErrors().addError(new ValidationError(config, "Constructor requires a value or a reference to an object or reference to a factory that generates objects"));
        }
        return getErrors();
    }
}
