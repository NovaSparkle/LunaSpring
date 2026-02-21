package org.novasparkle.lunaspring.API.conditions.abs;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

public interface Condition<E> {
    boolean check(E object, Object... objects);
    Object[] generateObjects(ConfigurationSection section);
    E cast(OfflinePlayer player);
    default boolean unknownCheck(ConfigurationSection section) {
        return false;
    }
}
