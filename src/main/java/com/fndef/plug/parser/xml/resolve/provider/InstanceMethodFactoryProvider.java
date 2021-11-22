package com.fndef.plug.parser.xml.resolve.provider;

import com.fndef.plug.Context;
import com.fndef.plug.parser.xml.resolve.Resolvable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiPredicate;

public class InstanceMethodFactoryProvider implements Provider<Object> {

    private final String refId;
    private final String methodName;
    private final Provider refProvider;

    private BiPredicate<String, Method> methodMatches = (mName, method) -> method.getName().equals(mName) && (method.getParameterCount() == 0);

    public InstanceMethodFactoryProvider(String refId, String method, Context context) {
        this.refId = refId;
        this.methodName = method;
        this.refProvider = () -> context.getById(refId, Resolvable.class);
    }

    @Override
    public Object get() {
        try {
            Object ref = refProvider.get();
            Objects.requireNonNull(ref, "Referenced factory object [ref="+refId+"] not found");

            Method[] methods = ref.getClass().getMethods();
            for (Method m : methods) {
                if (methodMatches.test(methodName, m)) {
                    m.setAccessible(true);
                    return m.invoke(ref);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException("Factory method ["+methodName+"] on object [id="+refId+"] could not be invoked");
        }
        throw new IllegalArgumentException("Factory method ["+methodName+"] for referenced object [id="+refId+"] not found");
    }
}
