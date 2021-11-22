package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.XmlConfig;

public class ConfigParameterValidation implements ValidatingConfigVisitor {

    private final XmlValidationRule typeExistsCheck = new TypeExistsCheck();
    private final XmlValidationRule typeSupportsMethodCheck = new MethodInTypeCheck();
    private final XmlValidationRule parentTypeSupportsMethodCheck = new MethodInParentTypeCheck();

    // check against self referencing ids
    private Errors errors = new Errors();

    @Override
    public Errors getErrors() {
        System.out.println("Config params validation has ["+errors.getErrors().size()+"] errors");
        System.out.println("Errors:");
        errors.getErrors().forEach(e -> System.out.println(e));
        return errors;
    }

    @Override
    public void factoryConfig(XmlConfig config) {
        errors = ValidationAggregate.of(typeExistsCheck)
                .nextValidation(typeSupportsMethodCheck)
                .validate(config)
                .merge(errors);
    }

    @Override
    public void objectConfig(XmlConfig config) {
        errors = ValidationAggregate.of(typeExistsCheck)
                .validate(config)
                .merge(errors);
    }

    @Override
    public void componentConfig(XmlConfig config) {

    }

    @Override
    public void controlConfig(XmlConfig config) {

    }

    @Override
    public void constructorConfig(XmlConfig config) {

    }

    @Override
    public void invokeConfig(XmlConfig config) {
        errors = ValidationAggregate.of(typeExistsCheck)
                .nextValidation(parentTypeSupportsMethodCheck)
                .validate(config)
                .merge(errors);
    }

    @Override
    public void configurationConfig(XmlConfig config) {
        // no checks required - structure already validated
    }

    @Override
    public void paramConfig(XmlConfig config) {

    }
}
