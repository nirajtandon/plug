package com.fndef.plug.parser.xml.resolve.provider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiPredicate;

public class StaticMethodFactoryProvider implements Provider {
    private String type;
    private String methodName;

    private BiPredicate<String, Method> matchesMethod = (mName, meth) -> meth.getName().equals(mName) && (meth.getParameterCount() == 0);

    public StaticMethodFactoryProvider(String type, String method) {
        this.type = type;
        this.methodName = method;
    }

    @Override
    public Object get() {
        try {
            Class c = Class.forName(type);
            Method[] methods = c.getMethods();
            for(Method m : methods) {
                if (matchesMethod.test(methodName, m)) {
                    m.setAccessible(true);
                    return m.invoke(null);
                }
            }
        } catch (ClassNotFoundException cnf) {
            // should not happen - types already verified
            throw new IllegalArgumentException("Type ["+type+"] not found");
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException("Method ["+ methodName +"] on type ["+type+"] could not be invoked", ex);
        }
        throw new IllegalArgumentException("Method ["+ methodName +"] on type ["+type+"] not found");
    }
}
