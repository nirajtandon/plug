package com.fndef.plug.parser.xml;

import com.fndef.plug.common.Errors;
import com.fndef.plug.common.ValidationException;
import com.fndef.plug.common.ConfigOptions;
import com.fndef.plug.Context;
import com.fndef.plug.parser.xml.resolve.ConfigResolver;
import com.fndef.plug.parser.xml.resolve.InitializingContext;
import com.fndef.plug.parser.xml.resolve.ResolvableOrder;
import com.fndef.plug.parser.xml.validation.ConfigParameterValidation;
import com.fndef.plug.parser.xml.validation.ConfigStructureValidation;
import com.fndef.plug.parser.xml.validation.RefIdValidator;
import com.fndef.plug.parser.xml.validation.ValidatingConfigVisitor;

import java.util.Objects;

import static com.fndef.plug.parser.xml.SimpleConfigWalker.WalkStrategy.POST;
import static com.fndef.plug.parser.xml.XmlConfigWalker.visitConfig;

public class ValidatingConfigResolver {

    private final ConfigOptions configOptions;
    private final XmlConfig configSet;
    private Errors validationErrors = new Errors();

    public ValidatingConfigResolver(ConfigOptions options, XmlConfig configSet) {
        Objects.requireNonNull(configSet, "Config is null");
        this.configOptions = options;
        this.configSet = configSet;
        System.out.println("Config is available? "+(configSet != null));
    }

    public ValidatingConfigResolver validateConfigStructure() {
        System.out.println("Validating config structure");
        validateConfig(new ConfigStructureValidation());
        return this;
    }

    public Context resolve() {
        if (validationErrors.hasErrors()) {
            throw new ValidationException(validationErrors);
        }
        return visitConfig(configSet, new ConfigResolver(initializingContext()), POST).getContext();
    }

    private InitializingContext initializingContext() {
        return new InitializingContext(visitConfig(configSet, new ResolvableOrder()).getResolveOrder());
    }

    public ValidatingConfigResolver validateConfigParameters() {
        // validate config params like
        // 1. references are to valid ids
        // 2. type exists and methods exist in classes - done
        // 3. return types of factories match the types of setters
        // 4. Constructors exist with the given args
        // 5. Value can be converted to the constructor or method type
        // 6. Method invocation should support multiple params - multiple values or ref or a mix (TODO)
        // 7. Convert value, and ref to nested tags which can be used within invocation or constructor (TODO)
        System.out.println("Validating parameters");
        validateConfig(new ConfigParameterValidation());
        return this;
    }

    public ValidatingConfigResolver validateReferences() {
        System.out.println("Validating references");
        validateConfig(new RefIdValidator());
        return this;
    }

    private void validateConfig(ValidatingConfigVisitor validator) {
        validationErrors = validationErrors.merge(visitConfig(configSet, validator).getErrors());
    }
}
