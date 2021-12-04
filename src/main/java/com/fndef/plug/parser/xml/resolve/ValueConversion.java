package com.fndef.plug.parser.xml.resolve;

import com.fndef.plug.Context;

import java.util.Objects;

public class ValueConversion {

    private ValueConversion() {}

    public static boolean isProvided(String name) {
        for (ProvidedConversionType p : ProvidedConversionType.values()) {
            if (p.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConverter(String name) {
        if (isProvided(name) || converterType(name)) {
            return true;
        }
        return false;
    }

    public static ValueConverter getConverter(String name, Context context) {
        Objects.requireNonNull(name, "Value converter is null");
        for (ProvidedConversionType p : ProvidedConversionType.values()) {
            if (p.name.equals(name)) {
                return p.converter;
            }
        }

        Objects.requireNonNull(context, "Context is required for referenced value converter");
        return new RefValueConverter(name, context);
    }

    private static boolean converterType(String name) {
        try {
            Class c = Class.forName(name);
            return ValueConverter.class.isAssignableFrom(c);
        } catch (ClassNotFoundException cnf) {
            throw new IllegalArgumentException("Class ["+name+"] not found", cnf);
        }
    }

    public enum ProvidedConversionType {
        NONE("none", new NoneConverter(), "No conversion"),
        INT("int", new StringToIntConverter(), "Convert to Integer"),
        LONG("long", new StringToLongConverter(), "Convert to Long"),
        DOUBLE("double", new StringToDoubleConverter(), "Convert to Double"),
        BOOL("bool", new StringToBoolConverter(), "Convert to Boolean");

        private String name;
        private ValueConverter converter;
        private String description;
        ProvidedConversionType(String type, ValueConverter converter, String description) {
            this.name = type;
            this.converter = converter;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public ValueConverter getConverter() {
            return converter;
        }

        public String getDescription() {
            return description;
        }
    }
}
