package org.novasparkle.lunaspring.API.util.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.utilities.lists.GenericList;
import org.novasparkle.lunaspring.API.util.utilities.lists.LunaLists;
import org.novasparkle.lunaspring.LunaSpring;

@UtilityClass
public class Localization {
    public IConfig getLocalizationConfig() {
        return new IConfig(LunaSpring.getInstance().getDataFolder(), "localization");
    }

    public String localize(EntityType type) {
        return getLocalizationConfig().getString(String.format("EntityType.%s", type.name()));
    }

    public String localize(Enchantment enchantment) {
        return getLocalizationConfig().getString(String.format("Enchantment.%s", enchantment.getKey().getKey().toUpperCase()));
    }

    public String localize(String path) {
        return getLocalizationConfig().getString("Custom." + path);
    }
}
