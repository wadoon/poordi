package com.github.wadoon;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Alexander Weigl
 * @version 1 (13.01.19)
 */
public abstract class AbstractRegistry implements Injector, Registry {
    @Override
    public <T> T getInstance(Class<T> clazz) throws InjectionException {
        for (var ctor : clazz.getConstructors()) {
            if (ctor.getAnnotation(Inject.class) != null) {
                T instance = (T) tryToInject(ctor);
                if (instance != null)
                    return instance;
            }
        }
        return null;
    }

    protected <T> T tryToInject(Constructor<T> ctor) throws InjectionException {
        var services = Arrays.stream(ctor.getParameterTypes())
                .map(this::get)
                .collect(Collectors.toUnmodifiableList());

        if (services.stream().allMatch(Objects::nonNull)) {
            try {
                return ctor.newInstance(services.toArray());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new InjectionException(e);
            }
        }
        return null;
    }

    @Override
    public void inject(Object instance) throws InjectionException {
        var clazz = instance.getClass();
        for (var setter : clazz.getMethods()) {
            if (setter.getAnnotation(Inject.class) != null) {
                inject(instance, setter);
            }
        }
    }

    protected void inject(Object instance, Method setter) throws InjectionException {
        if (setter.getParameterCount() != 1) {
            throw new IllegalStateException();
        }

        Class<?> pClazz = setter.getParameters()[0].getType();
        Object o = get(pClazz);
        if (o == null) {
            throw new IllegalStateException("Implementation for X not registered.");
        }
        try {
            setter.invoke(instance, o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InjectionException(e);
        }
    }
}
