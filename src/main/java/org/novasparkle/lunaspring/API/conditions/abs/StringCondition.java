package org.novasparkle.lunaspring.API.conditions.abs;

import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.Arrays;

public interface StringCondition extends PlayerCondition {
    @Override
    default boolean check(Player player, Object... objects) {
        String[] strings = Arrays.stream(objects)
                .filter(o -> o instanceof String)
                .map(o -> Utils.setPlaceholders(player, (String) o))
                .toArray(String[]::new);
        return check(player, strings);
    }

    boolean check(Player player, String[] strings);
}
