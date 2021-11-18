package com.fndef.plug.parser.xml.validation;

import java.util.Arrays;

public class ValidationUtils {
    public static boolean methodExists(String className, String methodName) {
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
