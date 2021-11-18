package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

import java.util.function.Predicate;

import static com.fndef.plug.parser.xml.validation.ValidationUtils.methodExists;

public class MethodInTypeCheck extends XmlValidationRule {
    private final Predicate<XmlConfig> hasType = config -> config.getAttributes().containsKey(AttributeType.TYPE.getAttrName());
    private final Predicate<XmlConfig> hasMethod = config -> config.getAttributes().containsKey(AttributeType.METHOD.getAttrName());

    public MethodInTypeCheck() {
        this(new Errors());
    }

    public MethodInTypeCheck(Errors errors) {
        super(errors);
    }

    @Override
    public Errors validate(XmlConfig config) {
        if (hasType.and(hasMethod).test(config)) {
            String className = config.getAttributes().get(AttributeType.TYPE.getAttrName());
            String methodName = config.getAttributes().get(AttributeType.METHOD.getAttrName());
            // TODO - check whould check for method params also
            if (!methodExists(className, methodName)) {
                getErrors().addError(new ValidationError(config, "Type ["+className+"] doesn't support method ["+methodName+"]"));
            }
        }
        return getErrors();
    }
}
