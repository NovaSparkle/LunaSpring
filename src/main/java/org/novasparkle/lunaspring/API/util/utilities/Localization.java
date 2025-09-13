package org.novasparkle.lunaspring.API.util.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.function.Function;

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

    public <E> E delocalize(Function<String, E> function, String sectionId, String translatedText) {
        ConfigurationSection section = getLocalizationConfig().getSection(sectionId);
        if (section == null) return null;

        for (String key : section.getKeys(false)) {
            if (translatedText.equalsIgnoreCase(section.getString(key))) {
                return function.apply(key);
            }
        }

        return null;
    }

    public EntityType delocalizeEntity(String translatedText) {
        return delocalize(s -> Utils.getEnumValue(EntityType.class, s), "EntityType", translatedText);
    }

    public Enchantment delocalizeEnchantment(String translatedText) {
        return delocalize(s -> Enchantment.getByKey(NamespacedKey.minecraft(s)), "Enchantment", translatedText);
    }

    public String delocalize(String translatedText) {
        return delocalize(s -> s, "Custom", translatedText);
    }
}
