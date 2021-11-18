package com.fndef.plug.parser.xml;

public enum AttributeType {
    ID("id", "Element unique Id"),
    REF("ref", "Reference to an Id"),
    FACTORY_REF("factory-ref", "Reference to a factory Id"),
    TYPE("type", "Class or a type"),
    METHOD("method", "Method name"),
    VALUE("value", "Static value");

    private String attrName;
    private String description;
    AttributeType(String attrName, String description) {
        this.attrName = attrName;
        this.description = description;
    }

    public String getAttrName() {
        return attrName;
    }

    public String getDescription() {
        return description;
    }

    public static boolean isValid(String attrName) {
        for(AttributeType attr : values()) {
            if (attr.getAttrName().equals(attrName)) {
                return true;
            }
        }
        return false;
    }

    public static AttributeType attributeType(String attrName) {
        for(AttributeType a : values()) {
            if (a.getAttrName().equals(attrName)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Invalid attribute type ["+attrName+"]");
    }
}
