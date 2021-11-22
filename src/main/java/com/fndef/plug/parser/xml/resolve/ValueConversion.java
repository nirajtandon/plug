package com.fndef.plug.parser.xml.resolve;

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

    public static ValueConverter getConverter(String name) {
        for (ProvidedConversionType p : ProvidedConversionType.values()) {
            if (p.name.equals(name)) {
                return p.converter;
            }
        }
        return createConverter(name);
    }

    private static boolean converterType(String name) {
        try {
            Class c = Class.forName(name);
            return ValueConverter.class.isAssignableFrom(c);
        } catch (ClassNotFoundException cnf) {
            throw new IllegalArgumentException("Class ["+name+"] not found", cnf);
        }
    }

    private static ValueConverter createConverter(String name) {
        try {
            if (converterType(name)) {
                Class c = Class.forName(name);
                Object o = c.newInstance();
                if (ValueConverter.class.isInstance(o)) {
                    return ValueConverter.class.cast(o);
                }
            }
            throw new IllegalArgumentException("Not a converter class ["+name+"]");
        } catch (ClassNotFoundException cnf) {
            throw new IllegalArgumentException("Class ["+name+"] not found", cnf);
        } catch (InstantiationException | IllegalAccessException ie) {
            throw new IllegalArgumentException("Converter ["+name+"] instantiation failed", ie);
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
