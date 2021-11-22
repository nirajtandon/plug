package com.fndef.plug.parser.xml.resolve.provider;

import com.fndef.plug.common.PrimitiveMapping;
import com.fndef.plug.parser.xml.resolve.Resolvable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TypeFactoryProvider implements Provider<Object> {

    private final String clazz;
    private List<Resolvable> constructors;
    private List<Object> params = new ArrayList<>();
    public TypeFactoryProvider(String clazz, List<Resolvable> constructors) {
        Objects.requireNonNull(constructors, "Constructors can't be null");
        this.clazz = clazz;
        this.constructors = constructors;
    }

    @Override
    public Object get() {
        try {
            Class c = Class.forName(clazz);
            resolveParams(constructors);
            Constructor[] constructors = c.getConstructors();
            for(Constructor con : constructors) {
                if (matches(con)) {
                    con.setAccessible(true);
                    return con.newInstance(params.toArray());
                }
            }
        } catch (ClassNotFoundException cnf) {
            throw new IllegalArgumentException("Class ["+clazz+"] not found", cnf);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
            throw new IllegalArgumentException("Class ["+clazz+"] failed instantiation using params ["+params+"]", ex);
        }
        throw new IllegalArgumentException("No matching constructor found for class ["+clazz+"]");
    }

    private boolean matches(Constructor constructor) {
        final int expectedCount = params.size();
        if (constructor.getParameterCount() == expectedCount) {
            Class[] paramTypes = constructor.getParameterTypes();
            for (int k = 0; k < paramTypes.length; k++) {
                Class pt = paramTypes[k];
                Object param = params.get(k);
                if (!pt.isInstance(param) && !PrimitiveMapping.isAssignable(pt, param.getClass())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void resolveParams(List<Resolvable> constructors) {
        for (Resolvable r : constructors) {
            Object o = r.resolve();
            Objects.requireNonNull(o, "Parameter not resolved while constructing factory type ["+clazz+"]");
            params.add(o);
        }
    }
}
