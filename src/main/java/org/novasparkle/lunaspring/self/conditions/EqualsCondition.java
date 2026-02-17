package org.novasparkle.lunaspring.self.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionId;
import org.novasparkle.lunaspring.API.conditions.abs.StringCondition;

@ConditionId("EQUALS")
public class EqualsCondition implements StringCondition {
    @Override
    public boolean check(Player player, String[] strings) {
        return strings.length >= 2 && strings[1].equals(strings[0]);
    }

    @Override
    public Object[] generateObjects(ConfigurationSection section) {
        return new Object[]{
                section.getString("input"),
                section.getString("check")
        };
    }
}
