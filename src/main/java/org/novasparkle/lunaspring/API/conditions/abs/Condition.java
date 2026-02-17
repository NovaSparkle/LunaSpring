package org.novasparkle.lunaspring.API.conditions.abs;

import org.bukkit.configuration.ConfigurationSection;

@FunctionalInterface
public interface Condition<E> {
    boolean check(E object, Object... objects);

    default boolean unknownCheck(ConfigurationSection section) {
        return true;
    }
}
