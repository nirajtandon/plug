package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.XmlConfig;

public class TagNestingCheck extends XmlValidationRule {

    public TagNestingCheck() {
        this(new Errors());
    }
    public TagNestingCheck(Errors errors) {
        super(errors);
    }

    @Override
    public Errors validate(XmlConfig config) {
        config.getChildEntries().stream()
                .filter(cs -> ! config.getTagType().supportedNestedTag().contains(cs.getTagType()))
                .map(cs -> new ValidationError(cs, "Tag ["+config.getName()+"] doesn't support nested tag ["+cs.getName()+"]"))
                .forEach(e -> getErrors().addError(e));
        return getErrors();
    }
}
