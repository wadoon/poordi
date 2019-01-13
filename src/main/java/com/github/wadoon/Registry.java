package com.github.wadoon;

/**
 * @author Alexander Weigl
 * @version 1 (13.01.19)
 */
public interface Registry {
    <T> Object register(Class<T> clazz, T instance);
    <T> T get(Class<T> clazz);
}
