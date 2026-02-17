package org.novasparkle.lunaspring.self.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionId;
import org.novasparkle.lunaspring.API.conditions.abs.PlayerCondition;

@ConditionId("PERMISSION")
public class PermissionCondition implements PlayerCondition {
    @Override
    public boolean check(Player player, Object... objects) {
        return objects.length >= 1 && player.hasPermission((String) objects[0]);
    }

    @Override
    public Object[] generateObjects(ConfigurationSection section) {
        return new Object[]{section.getString("permission")};
    }
}
