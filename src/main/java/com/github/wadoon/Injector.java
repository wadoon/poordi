package com.github.wadoon;

/**
 * @author Alexander Weigl
 * @version 1 (13.01.19)
 */
public interface Injector {
    <T> T getInstance(Class<T> clazz) throws InjectionException;

    void inject(Object instance) throws InjectionException;
}
