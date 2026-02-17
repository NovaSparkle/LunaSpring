package org.novasparkle.lunaspring.self.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionId;
import org.novasparkle.lunaspring.API.conditions.abs.PlayerCondition;
import org.novasparkle.lunaspring.API.util.service.managers.VaultManager;

@ConditionId("MONEY")
public class MoneyCondition implements PlayerCondition {
    @Override
    public boolean check(Player player, Object... objects) {
        return VaultManager.hasEnoughMoney(player, (double) objects[0]);
    }

    @Override
    public Object[] generateObjects(ConfigurationSection section) {
        return new Object[]{section.getDouble("money")};
    }
}
