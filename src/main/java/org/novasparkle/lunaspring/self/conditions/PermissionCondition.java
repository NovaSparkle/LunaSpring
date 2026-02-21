package org.novasparkle.lunaspring.self.conditions;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permissible;
import org.novasparkle.lunaspring.API.conditions.abs.Condition;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionId;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

@ConditionId("PERMISSION")
public class PermissionCondition implements Condition<Permissible> {
    @Override
    public boolean check(Permissible permissible, Object... objects) {
        if (objects.length < 1) return false;

        String permission = (String) objects[0];
        if (permissible instanceof OfflinePlayer p) permission = Utils.setPlaceholders(p, permission);

        return permissible.hasPermission(permission);
    }

    @Override
    public Object[] generateObjects(ConfigurationSection section) {
        return new Object[]{section.getString("permission")};
    }

    @Override
    public Permissible cast(OfflinePlayer player) {
        return player instanceof Permissible p ? p : null;
    }
}
