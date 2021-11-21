package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class DupIdCheck extends XmlValidationRule {

    private final Set<String> seen = new HashSet<>();
    private final Predicate<String> isSeen = id -> seen.contains(id);

    DupIdCheck() {
        super(new Errors());
    }

    DupIdCheck(Errors errors) {
        super((errors));
    }

    @Override
    public Errors validate(XmlConfig config) {
        final String id = config.getAttributes().get(AttributeType.ID.getAttrName());
        if (isSeen.test(id)) {
            getErrors().addError(new ValidationError(config, "Tag ["+config.getName()+"] uses Id ["+id+"] that is already used"));
        }
        return getErrors();
    }
}
