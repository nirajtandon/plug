package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.AttributeType;
import com.fndef.plug.parser.xml.XmlConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class RefIdValidator implements ValidatingConfigVisitor {

    private final Errors errors = new Errors();

    private final Map<String, Set<ValidationError>> unresolvedConfigs = new HashMap<>();
    private final Map<String, XmlConfig> idMap = new HashMap<>();

    private final Predicate<XmlConfig> hasId = config -> config.getAttributes().containsKey(AttributeType.ID.getAttrName());

    @Override
    public Errors getErrors() {
        unresolvedConfigs.values().stream()
                .flatMap(errors -> errors.stream())
                .forEach(e -> errors.addError(e));
        System.out.println("Ref/Id validation has ["+errors.getErrors().size()+"] errors");
        System.out.println("Errors:");
        errors.getErrors().forEach(e -> System.out.println(e));
        return errors;
    }

    @Override
    public void factoryConfig(XmlConfig config) {
        resolveConfig(config);
    }

    @Override
    public void objectConfig(XmlConfig config) {
        resolveConfig(config);
    }

    @Override
    public void componentConfig(XmlConfig config) {
        resolveConfig(config);
    }

    @Override
    public void controlConfig(XmlConfig config) {
        resolveConfig(config);
    }

    @Override
    public void constructorConfig(XmlConfig config) {
        resolveConfig(config); // this will not have ref / factory ref - use nested tags
    }

    @Override
    public void invokeConfig(XmlConfig config) {
        resolveConfig(config); // this will not have ref /factory ref - use nested tags
    }

    @Override
    public void configurationConfig(XmlConfig config) {
        // this will not have ref
    }

    private void resolveConfig(XmlConfig config) {
        resolveOrWait(AttributeType.REF, config);
        resolveOrWait(AttributeType.FACTORY_REF, config);
    }

    private void resolveOrWait(AttributeType type, XmlConfig config) {
        addAndResolve(config);
        resolveRef(type, config);
    }

    private void resolveRef(AttributeType type, XmlConfig config) {
        config.getAttributes().keySet().stream()
                .filter(k -> type.getAttrName().equals(k))
                .map(k -> config.getAttributes().get(k))
                .filter(v -> ! idMap.containsKey(v))
                .forEach(v -> addUnresolved(v, config));
    }

    private void addAndResolve(XmlConfig config) {
        if (hasId.test(config)) {
            final String id = config.getAttributes().get(AttributeType.ID.getAttrName());
            idMap.putIfAbsent(id, config);
            unresolvedConfigs.remove(id);
        }
    }

    private void addUnresolved(String ref, XmlConfig config) {
        unresolvedConfigs.putIfAbsent(ref, new HashSet<>());
        Set<ValidationError> configs = unresolvedConfigs.get(ref);
        configs.add(new ValidationError(config, "Tag ["+config.getName()+"] references id ["+ref+"] which is unresolved"));
    }

}
