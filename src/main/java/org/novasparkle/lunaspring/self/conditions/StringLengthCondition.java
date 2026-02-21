package org.novasparkle.lunaspring.self.conditions;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionId;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionNullable;
import org.novasparkle.lunaspring.API.conditions.abs.StringCondition;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;

@ConditionId("STRING_LENGTH") @ConditionNullable
public class StringLengthCondition implements StringCondition {
    @Override
    public boolean check(OfflinePlayer player, String[] strings) {
        if (strings.length == 0) return false;

        int length = strings[0].length();
        if (strings.length == 1) return length > 0;

        int min = LunaMath.toInt(strings[1]);
        if (strings.length == 2) {
            return length >= min;
        }

        int max = LunaMath.toInt(strings[2]);
        return length >= min && length <= max;
    }

    @Override
    public Object[] generateObjects(ConfigurationSection section) {
        return new Object[]{
                section.getString("input"),
                section.getString("min"),
                section.getString("max")
        };
    }
}
