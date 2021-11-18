package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

import java.util.function.Predicate;

public class FactoryMethodCheck extends XmlValidationRule {

    private final Predicate<XmlConfig> hasType = config -> config.getAttributes().containsKey(AttributeType.TYPE.getAttrName());
    private final Predicate<XmlConfig> hasMethod = config -> config.getAttributes().containsKey(AttributeType.METHOD.getAttrName());
    private final Predicate<XmlConfig> hasRef = config -> config.getAttributes().containsKey(AttributeType.REF.getAttrName());
    private final Predicate<XmlConfig> typeWithMethod = config -> hasType.and(hasMethod).test(config);
    private final Predicate<XmlConfig> refWithMethod = config -> hasRef.and(hasMethod).test(config);
    private final Predicate<XmlConfig> typeWithRef = config -> hasType.and(hasRef).test(config);
    public FactoryMethodCheck() {
        this(new Errors());
    }

    public FactoryMethodCheck(Errors errors) {
        super(errors);
    }

    @Override
    public Errors validate(XmlConfig config) {
        if (! typeWithMethod.or(refWithMethod).or(hasType).test(config)) {
            getErrors().addError(new ValidationError(config, "Factory needs attributes type and method or attributes ref and method - or a type that can be directly instantiated"));
        }

        if (typeWithRef.test(config)) {
            getErrors().addError(new ValidationError(config, "Factory can't have type and ref in the same tag"));
        }
        return getErrors();
    }
}
