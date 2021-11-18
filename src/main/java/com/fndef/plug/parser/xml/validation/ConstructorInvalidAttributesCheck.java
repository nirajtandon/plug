package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

import java.util.function.Predicate;

public class ConstructorInvalidAttributesCheck extends XmlValidationRule {
    public ConstructorInvalidAttributesCheck() {
        this(new Errors());
    }

    public ConstructorInvalidAttributesCheck(Errors errors) {
        super(errors);
    }

    private Predicate<String> isRef = attr -> AttributeType.REF.getAttrName().equals(attr);
    private Predicate<String> isValue = attr -> AttributeType.VALUE.getAttrName().equals(attr);
    private Predicate<String> isFactoryRef = attr -> AttributeType.FACTORY_REF.getAttrName().equals(attr);

    @Override
    public Errors validate(XmlConfig config) {
        long attrCnt = config.getAttributes().keySet().stream()
                .filter(k -> isValue.or(isRef).or(isFactoryRef).test(k))
                .count();

        if (attrCnt > 1) {
            getErrors().addError(new ValidationError(config, "Constructor tag supports one of value / ref / factory-ref attributes"));
        }
        return getErrors();
    }
}
