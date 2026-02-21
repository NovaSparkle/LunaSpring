package org.novasparkle.lunaspring.self.conditions;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionId;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionNullable;
import org.novasparkle.lunaspring.API.conditions.abs.StringCondition;

@ConditionId("STARTS_WITH") @ConditionNullable
public class StartsWithCondition implements StringCondition {
    @Override
    public boolean check(OfflinePlayer player, String[] strings) {
        return strings[0].startsWith(strings[1]);
    }

    @Override
    public Object[] generateObjects(ConfigurationSection section) {
        return new Object[]{
                section.getString("input"),
                section.getString("check")
        };
    }
}
