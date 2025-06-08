package org.novasparkle.lunaspring.API.util.utilities;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.LunaSpring;

public class Localization {
    public static String localize(EntityType type) {
        return new IConfig(LunaSpring.getINSTANCE().getDataFolder(), "localization").getString(String.format("EntityType.%s", type.name()));
    }

    public static String localize(Enchantment enchantment) {
        return new IConfig(LunaSpring.getINSTANCE().getDataFolder(), "localization").getString(String.format("Enchantment.%s", enchantment.getKey().getKey()));
    }
}
