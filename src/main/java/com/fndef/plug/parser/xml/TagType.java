package com.fndef.plug.parser.xml;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static com.fndef.plug.parser.xml.AttributeType.CONVERTER;
import static com.fndef.plug.parser.xml.AttributeType.FACTORY_REF;
import static com.fndef.plug.parser.xml.AttributeType.ID;
import static com.fndef.plug.parser.xml.AttributeType.METHOD;
import static com.fndef.plug.parser.xml.AttributeType.REF;
import static com.fndef.plug.parser.xml.AttributeType.TYPE;
import static com.fndef.plug.parser.xml.AttributeType.VALUE;

public enum TagType {
    CONFIGURATION("configuration", true,  "Configuration root") {
        @Override
        public Set<TagType> supportedNestedTag() {
            return EnumSet.of(FACTORY, OBJECT, COMPONENT, CONTROL);
        }

        @Override
        public Set<AttributeType> supportedAttributes() {
            return new HashSet<>();
        }
    },
    FACTORY("factory", true,  "Factory configuration") {
        @Override
        public Set<TagType> supportedNestedTag() {
            return EnumSet.of(CONSTRUCTOR, INVOKE);
        }

        @Override
        public Set<AttributeType> supportedAttributes() {
            return EnumSet.of(ID, TYPE, METHOD, REF);
        }
    },
    OBJECT("object", true,  "Object configuration") {
        @Override
        public Set<TagType> supportedNestedTag() {
            return EnumSet.of(CONSTRUCTOR, INVOKE);
        }

        @Override
        public Set<AttributeType> supportedAttributes() {
            return EnumSet.of(ID, TYPE, FACTORY_REF, REF);
        }
    },
    COMPONENT("component", true, "Component definition") {
        @Override
        public Set<TagType> supportedNestedTag() {
            return EnumSet.of(CONSTRUCTOR, INVOKE);
        }

        @Override
        public Set<AttributeType> supportedAttributes() {
            return EnumSet.of(ID, TYPE);
        }
    },
    CONTROL("control", true,  "Control definition") {
        @Override
        public Set<TagType> supportedNestedTag() {
            return EnumSet.of(CONSTRUCTOR, INVOKE);
        }

        @Override
        public Set<AttributeType> supportedAttributes() {
            return EnumSet.of(ID, TYPE);
        }
    },
    CONSTRUCTOR("constructor", false,  "Constructor entry") {
        @Override
        public Set<TagType> supportedNestedTag() {
            return new HashSet<>();
        }

        @Override
        public Set<AttributeType> supportedAttributes() {
            return EnumSet.of(VALUE, FACTORY_REF, REF, CONVERTER);
        }
    },
    INVOKE("invoke", false,  "Invocation entry") {
        @Override
        public Set<TagType> supportedNestedTag() {
            return EnumSet.of(PARAM);
        }

        @Override
        public Set<AttributeType> supportedAttributes() {
            return EnumSet.of(METHOD);
        }
    },
    PARAM("param", false, "Exact value") {
        @Override
        public Set<TagType> supportedNestedTag() {
            return new HashSet<>();
        }

        @Override
        public Set<AttributeType> supportedAttributes() {
            return EnumSet.of(VALUE, FACTORY_REF, REF, CONVERTER);
        }
    },
    SHUTDOWN("shutdown", false, "shutdown hook") {
        @Override
        public Set<TagType> supportedNestedTag() {
            return new HashSet<>();
        }

        @Override
        public Set<AttributeType> supportedAttributes() {
            return EnumSet.of(METHOD);
        }
    };

    private String name;
    private boolean topLevel;
    private String description;
    TagType(String name, boolean topLevel, String description) {
        this.name = name;
        this.topLevel = topLevel;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public boolean isTopLevel() {
        return topLevel;
    }

    public String getDescription() {
        return description;
    }

    public abstract Set<TagType> supportedNestedTag();

    public abstract Set<AttributeType> supportedAttributes();

    public static TagType tag(String name) {
        for (TagType t : values()) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid tag ["+name+"]");
    }

    public static boolean isTagValid(String tagName) {
        for(TagType tag : TagType.values()) {
            if (tag.getName().equals(tagName)) {
                return true;
            }
        }
        return false;
    }
}
