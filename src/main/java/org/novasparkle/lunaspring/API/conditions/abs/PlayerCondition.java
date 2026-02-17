package org.novasparkle.lunaspring.API.conditions.abs;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public interface PlayerCondition extends Condition<Player> {
    Object[] generateObjects(ConfigurationSection section);
}
