package weigl.poordi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Registry {
    private Map<Class<?>, Object> mapping = new HashMap<>();

    public <T> Object register(Class<T> clazz, T instance) {
        return mapping.put(clazz, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        return (T) mapping.get(clazz);
    }

    public <T> T getInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        for (var ctor : clazz.getConstructors()) {
            if (ctor.getAnnotation(Inject.class) != null) {
                T instance = (T) tryToInject(ctor);
                if (instance != null)
                    return instance;
            }
        }
        return null;
    }

    private <T> T tryToInject(Constructor<T> ctor) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        var services = Arrays.stream(ctor.getParameterTypes())
                .map(this::get)
                .collect(Collectors.toUnmodifiableList());

        if (services.stream().allMatch(Objects::nonNull)) {
            return ctor.newInstance(services.toArray());
        }
        return null;
    }

    public void inject(Object instance) throws InvocationTargetException, IllegalAccessException {
        var clazz = instance.getClass();
        for (var setter : clazz.getMethods()) {
            if (setter.getAnnotation(Inject.class) != null) {
                inject(instance, setter);
            }
        }
    }

    private void inject(Object instance, Method setter) throws InvocationTargetException, IllegalAccessException {
        if (setter.getParameterCount() != 1) {
            throw new IllegalStateException();
        }

        Class<?> pClazz = setter.getParameters()[0].getType();
        Object o = get(pClazz);
        if (o == null) {
            throw new IllegalStateException("Implementation for X not registered.");
        }

        setter.invoke(instance, o);
    }
}