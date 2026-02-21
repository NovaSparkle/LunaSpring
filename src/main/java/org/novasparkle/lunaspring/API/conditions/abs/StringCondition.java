package org.novasparkle.lunaspring.API.conditions.abs;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.Arrays;
import java.util.function.Function;

public interface StringCondition extends OfflinePlayerCondition {
    boolean check(@Nullable OfflinePlayer player, String[] strings);

    @Override
    default boolean check(OfflinePlayer player, Object... objects) {
        boolean hasPlayer = player != null;

        Function<Object, String> func = hasPlayer ?
                o -> Utils.setPlaceholders(player, (String) o) :
                o -> (String) o;
        String[] strings = Arrays.stream(objects)
                .filter(o -> o instanceof String)
                .map(func)
                .toArray(String[]::new);
        return check(player, strings);
    }

    @Override
    default boolean unknownCheck(ConfigurationSection section) {
        Object[] objects = generateObjects(section);
        if (objects == null || objects.length == 0) return false;

        return check(null, objects);
    }
}
