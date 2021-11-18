package com.fndef.plug.parser.xml.resolve.provider;

import com.fndef.plug.parser.xml.resolve.Resolvable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TypeFactoryProvider implements Provider {

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
                    System.out.println("constructor matches");
                    con.setAccessible(true);
                    return con.newInstance(params.toArray());
                }
            }
        } catch (ClassNotFoundException cnf) {
            throw new IllegalArgumentException("Factory class ["+clazz+"] not found", cnf);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
            throw new IllegalArgumentException("Factory class ["+clazz+"] failed instantiation using params ["+params+"]", ex);
        }
        throw new IllegalArgumentException("Factory class ["+clazz+"] could not be instantiated");
    }

    private boolean matches(Constructor constructor) {
        final int expectedCount = params.size();
        if (constructor.getParameterCount() == expectedCount) {
            Class[] paramTypes = constructor.getParameterTypes();
            for (int k = 0; k < paramTypes.length; k++) {
                if (! paramTypes[k].isInstance(params.get(k))) {
                    System.out.println("constructor doesn't match");
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
            System.out.println("resolving params - "+o);
            params.add(o);
        }
    }
}
