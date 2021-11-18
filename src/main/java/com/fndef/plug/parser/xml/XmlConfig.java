package com.fndef.plug.parser.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlConfig {
    private String id;
    private XmlConfig parent;
    private final String name;
    private final TagType tagType;
    private final Map<String, String> attributes = new HashMap<>();
    private final List<XmlConfig> childEntries = new ArrayList<>();

    public XmlConfig(String name, TagType elementType) {
        this.name = name;
        this.tagType = elementType;
    }

    public void addEntry(XmlConfig entry) {
        childEntries.add(entry);
    }

    public void addAttribute(String name, String value) {
        if ("id".equals(name)) {
            id = value;
        }
        attributes.put(name, value);
    }

    public List<XmlConfig> getChildEntries() {
        return childEntries;
    }

    public TagType getTagType() {
        return tagType;
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes);
    }

    public String toString() {
        return "ConfigEntry ["+ tagType +"], attributes="+ attributes;
    }

    public String getName() {
        return name;
    }

    public void accept(XmlConfigVisitor visitor) {
        switch (getTagType()) {
            case FACTORY:
                visitor.factoryConfig(this);
                break;
            case OBJECT:
                visitor.objectConfig(this);
                break;
            case COMPONENT:
                visitor.componentConfig(this);
                break;
            case CONTROL:
                visitor.controlConfig(this);
                break;
            case CONFIGURATION:
                visitor.configurationConfig(this);
                break;
            case INVOKE:
                visitor.invokeConfig(this);
                break;
            case CONSTRUCTOR:
                visitor.constructorConfig(this);
                break;
            default:
                throw new IllegalArgumentException("Unsupported config type");
        }
    }

    public XmlConfig getParent() {
        return parent;
    }

    public void setParent(XmlConfig config) {
        this.parent = config;
    }
}
