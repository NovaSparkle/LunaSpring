package org.novasparkle.lunaspring.self.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionId;
import org.novasparkle.lunaspring.API.conditions.abs.PlayerCondition;

@ConditionId("EXP")
public class ExpCondition implements PlayerCondition {
    @Override
    public boolean check(Player player, Object... objects) {
        if (objects.length == 1) {
            return player.getTotalExperience() >= (int) objects[0];
        }

        if (objects.length >= 2) {
            if ((boolean) objects[1]) return player.getLevel() >= (int) objects[0];
            else return player.getTotalExperience() >= (int) objects[0];
        }

        return false;
    }

    @Override
    public Object[] generateObjects(ConfigurationSection section) {
        return new Object[]{
                section.getInt("exp"),
                section.getBoolean("checkLevel")
        };
    }
}
