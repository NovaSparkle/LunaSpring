package org.novasparkle.lunaspring.self.conditions;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionId;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionNullable;
import org.novasparkle.lunaspring.API.conditions.abs.StringCondition;

@ConditionId("EQUALS_IGNORE_CASE") @ConditionNullable
public class EqualsIgnoreCaseCondition implements StringCondition {
    @Override
    public boolean check(OfflinePlayer player, String[] strings) {
        return strings.length >= 2 && strings[0].equalsIgnoreCase(strings[1]);
    }

    @Override
    public Object[] generateObjects(ConfigurationSection section) {
        return new Object[]{
                section.getString("input"),
                section.getString("check")
        };
    }
}
