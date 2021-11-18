package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

import static com.fndef.plug.parser.xml.validation.ValidationUtils.methodExists;

public class MethodInParentTypeCheck extends XmlValidationRule {
    public MethodInParentTypeCheck() {
        this(new Errors());
    }

    public MethodInParentTypeCheck(Errors errors) {
        super(errors);
    }

    @Override
    public Errors validate(XmlConfig config) {

        // TODO - this should additionally validate that the method params are valid not just the method name

        final String className = config.getParent().getAttributes().get(AttributeType.TYPE.getAttrName());
        config.getAttributes().keySet().stream()
                .filter(k -> AttributeType.METHOD.getAttrName().equals(k))
                .map(k -> config.getAttributes().get(k))
                .filter(mName -> ! methodExists(className, mName))
                .map(mName -> new ValidationError(config, "Type defined in tag ["+config.getParent().getName()+"] has the required method ["+mName+"] missing"))
                .forEach(e -> getErrors().addError(e));

        return getErrors();
    }

}
