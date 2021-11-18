package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

public class ConfigStructureValidation implements ValidatingConfigVisitor {

    private final XmlValidationRule requiredIdAttributeCheck = new RequiredAttributeCheck(AttributeType.ID);
    private final XmlValidationRule requiredMethodAttributeCheck = new RequiredAttributeCheck(AttributeType.METHOD);
    private final XmlValidationRule tagNestingCheck = new TagNestingCheck();
    private final XmlValidationRule validAttributeCheck = new ValidAttributeCheck();
    private final XmlValidationRule tagSupportsAttributeCheck = new TagSupportsAttributeCheck();
    private final XmlValidationRule factoryMethodCheck = new FactoryMethodCheck();
    private final XmlValidationRule constructorAttributesCheck = new ConstructorAttributesCheck();
    private final XmlValidationRule constructorInvalidAttrCheck = new ConstructorInvalidAttributesCheck();
    private final XmlValidationRule noAttributesCheck = new NoAttributesCheck();

    private Errors errors;

    public ConfigStructureValidation(Errors errors) {
        this.errors = errors;
    }

    public ConfigStructureValidation() {
        this(new Errors());
    }

    @Override
    public void factoryConfig(XmlConfig config) {
        errors = ValidationAggregate.of(requiredIdAttributeCheck)
                .nextValidation(tagNestingCheck)
                .nextValidation(validAttributeCheck)
                .nextValidation(tagSupportsAttributeCheck)
                .nextValidation(factoryMethodCheck)
                .validate(config)
                .merge(errors);

        // factory can have invoke for all factory types to support object initialization by factory
    }

    @Override
    public void objectConfig(XmlConfig config) {
        errors = ValidationAggregate.of(requiredIdAttributeCheck)
                .nextValidation(tagNestingCheck)
                .nextValidation(validAttributeCheck)
                .nextValidation(tagSupportsAttributeCheck)
                .validate(config)
                .merge(errors);
    }

    @Override
    public void componentConfig(XmlConfig config) {
        errors = ValidationAggregate.of(requiredIdAttributeCheck)
                .nextValidation(tagNestingCheck)
                .nextValidation(validAttributeCheck)
                .nextValidation(tagSupportsAttributeCheck)
                .validate(config)
                .merge(errors);
    }

    @Override
    public void controlConfig(XmlConfig config) {
        errors = ValidationAggregate.of(requiredIdAttributeCheck)
                .nextValidation(new TagNestingCheck())
                .nextValidation(new ValidAttributeCheck())
                .nextValidation(new TagSupportsAttributeCheck())
                .validate(config)
                .merge(errors);
    }

    @Override
    public void invokeConfig(XmlConfig config) {
        errors = ValidationAggregate.of(tagNestingCheck)
                .nextValidation(validAttributeCheck)
                .nextValidation(tagSupportsAttributeCheck)
                .nextValidation(requiredMethodAttributeCheck)
                .validate(config)
                .merge(errors);
    }

    @Override
    public void constructorConfig(XmlConfig config) {
        errors = ValidationAggregate.of(tagNestingCheck)
                .nextValidation(validAttributeCheck)
                .nextValidation(tagSupportsAttributeCheck)
                .nextValidation(constructorAttributesCheck)
                .nextValidation(constructorInvalidAttrCheck)
                .validate(config)
                .merge(errors);
    }

    @Override
    public void configurationConfig(XmlConfig config) {
        errors = ValidationAggregate.of(noAttributesCheck)
                .nextValidation(tagNestingCheck)
                .nextValidation(tagSupportsAttributeCheck)
                .validate(config)
                .merge(errors);
    }

    public Errors getErrors() {
        System.out.println("Config structure validation has ["+errors.getErrors().size()+"] errors");
        System.out.println("Errors :");
        errors.getErrors().forEach(e -> System.out.println(e));
        return errors;
    }
}
