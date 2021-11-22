package com.fndef.plug.parser.xml.validation;

import java.util.Arrays;
import java.util.Objects;

public class ValidationUtils {
    public static boolean methodExists(String className, String methodName) {
        Objects.requireNonNull(className, "Class name is required");
        Objects.requireNonNull(methodName, "Method name is required");
        try {
            return Arrays.stream(Class.forName(className).getMethods())
                    .anyMatch(m -> m.getName().equals(methodName));
        } catch (ClassNotFoundException cnf) {
            return false;
        }
    }


    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException cnf) {
            return false;
        }
    }
}
