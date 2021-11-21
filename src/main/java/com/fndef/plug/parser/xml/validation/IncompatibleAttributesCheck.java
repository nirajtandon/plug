package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.XmlConfig;

import java.util.function.Predicate;

import static com.fndef.plug.parser.xml.AttributeType.REF;
import static com.fndef.plug.parser.xml.AttributeType.FACTORY_REF;

public class IncompatibleAttributesCheck extends XmlValidationRule {

    private final Predicate<XmlConfig> hasRef = config -> config.getAttributes().containsKey(REF.getAttrName());
    private final Predicate<XmlConfig> hasFactoryRef = config -> config.getAttributes().containsKey(FACTORY_REF.getAttrName());

    IncompatibleAttributesCheck() {
        super(new Errors());
    }

    IncompatibleAttributesCheck(Errors errors) {
        super(errors);
    }

    @Override
    public Errors validate(XmlConfig config) {
        if (hasRef.and(hasFactoryRef).test(config)) {
            getErrors().addError(new ValidationError(config, "Tag ["+config.getName()+"] uses both factory-ref and ref"));
        }
        return getErrors();
    }
}
