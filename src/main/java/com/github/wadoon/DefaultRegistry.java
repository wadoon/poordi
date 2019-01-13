package com.github.wadoon;

import java.util.HashMap;
import java.util.Map;

public class DefaultRegistry extends AbstractRegistry {
    private Map<Class<?>, Object> mapping = new HashMap<>();

    @Override
    public <T> Object register(Class<T> clazz, T instance) {
        return mapping.put(clazz, instance);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        return (T) mapping.get(clazz);
    }
}