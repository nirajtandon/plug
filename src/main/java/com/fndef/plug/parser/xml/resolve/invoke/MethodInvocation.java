package com.fndef.plug.parser.xml.resolve.invoke;

import com.fndef.plug.common.PrimitiveMapping;
import com.fndef.plug.parser.xml.resolve.Resolvable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodInvocation implements Invocation {
    private final String methodName;
    private final List<Resolvable> resolvableParams = new ArrayList<>();

    public MethodInvocation(String methodName, List<Resolvable> params) {
        Objects.requireNonNull(methodName, "Method is null");
        Objects.requireNonNull(params, "Method params are null");
        this.methodName = methodName;
        resolvableParams.addAll(params);
    }

    @Override
    public void invoke(Object o) {
        Objects.requireNonNull(o, "Method invocation target is null");
        try {
            List<Object> invokeParams = resolvableParams.stream().map(r -> r.resolve()).collect(Collectors.toList());
            Class c = o.getClass();
            Method[] methods = c.getMethods();
            Method m = Arrays.stream(methods).filter(method -> matches(method, invokeParams)).findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching method found for ["+methodName+"]"));

            m.setAccessible(true);
            m.invoke(o, invokeParams.toArray());
        } catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Method invocation failed", ex);
        }
    }

    private boolean matches(Method m, List<Object> params) {
        if (methodName.equals(m.getName()) && m.getParameterCount() == params.size()) {
            Class[] paramTypes = m.getParameterTypes();
            for (int k = 0; k < params.size(); k++) {
                Class type = paramTypes[k];
                Object param = params.get(k);
                if (! type.isInstance(param) && !PrimitiveMapping.isAssignable(type, param.getClass())) {
                   return false;
                }
            }
            return true;
        }
        return false;
    }
}
