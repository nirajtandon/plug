package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

import static com.fndef.plug.parser.xml.validation.ValidationUtils.classExists;

public class TypeExistsCheck extends XmlValidationRule {
    public TypeExistsCheck() {
        this(new Errors());
    }

    public TypeExistsCheck(Errors errors) {
        super(errors);
    }

    @Override
    public Errors validate(XmlConfig config) {
        config.getAttributes().keySet().stream()
                .filter(k -> AttributeType.TYPE.getAttrName().equals(k))
                .filter(k -> ! classExists(config.getAttributes().get(k)))
                .map(k -> new ValidationError(config, "Tag ["+config.getName()+"] refers to type ["+config.getAttributes().get(k)+"] which doesn't exist"))
                .forEach(e -> getErrors().addError(e));
        return getErrors();
    }

}
