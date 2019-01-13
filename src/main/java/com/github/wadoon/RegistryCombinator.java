package com.github.wadoon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (13.01.19)
 */
public class RegistryCombinator extends AbstractRegistry {
    private List<Registry> registries = new ArrayList<>();
    private DefaultRegistry writeTo = new DefaultRegistry();

    public RegistryCombinator() {
        registries.add(writeTo);
    }

    public RegistryCombinator(Registry... seq) {
        this();
        Collections.addAll(registries, seq);
    }

    @Override
    public <T> Object register(Class<T> clazz, T instance) {
        return writeTo.register(clazz, instance);
    }

    @Override
    public <T> T get(Class<T> clazz) {
        for (Registry r : registries) {
            T val = r.get(clazz);
            if (val != null) return val;
        }
        return null;
    }
}
