package org.novasparkle.lunaspring.API.util.utilities;

@FunctionalInterface
public interface Returner<T> {
    T apply();
}
