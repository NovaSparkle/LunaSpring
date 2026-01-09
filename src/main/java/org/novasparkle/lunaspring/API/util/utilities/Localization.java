package org.novasparkle.lunaspring.API.util.utilities;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class Localization {
    public final String ENTITY_TYPE_PATH = "EntityType";
    public final String ENCHANT_PATH = "Enchantment";
    public final String CUSTOM_PATH = "Custom";

    public IConfig getLocalizationConfig() {
        return new IConfig(LunaSpring.getInstance().getDataFolder(), "localization");
    }

    public String localize(EntityType type) {
        return getLocalizationConfig().getString(String.format("%s.%s", ENTITY_TYPE_PATH, type.name()));
    }

    public String localize(Enchantment enchantment) {
        return getLocalizationConfig().getString(String.format("%s.%s", ENCHANT_PATH, enchantment.getKey().getKey().toUpperCase()));
    }

    public String localize(String path) {
        return getLocalizationConfig().getString(String.format("%s.%s", CUSTOM_PATH, path));
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
        return delocalize(s -> Utils.getEnumValue(EntityType.class, s), ENTITY_TYPE_PATH, translatedText);
    }

    public Enchantment delocalizeEnchantment(String translatedText) {
        return delocalize(Utils::getEnchantment, ENCHANT_PATH, translatedText);
    }

    public String delocalize(String translatedText) {
        return delocalize(s -> s, CUSTOM_PATH, translatedText);
    }

    public <E> Map<E, String> localizeToMap(Function<String, E> function, String sectionId) {
        ConfigurationSection section = getLocalizationConfig().getSection(sectionId);
        if (section == null) return null;

        Map<E, String> map = Maps.newHashMap();
        for (String key : section.getKeys(false)) {
            E object = function.apply(key);
            if (object != null) map.put(object, section.getString(key));
        }

        return map;
    }

    public Map<Enchantment, String> localizeToEnchantmentMap() {
        return localizeToMap(Utils::getEnchantment, ENCHANT_PATH);
    }

    public Map<EntityType, String> localizeToEntityTypeMap() {
        return localizeToMap(k -> Utils.getEnumValue(EntityType.class, k), ENTITY_TYPE_PATH);
    }

    public Map<String, String> localizeToMap() {
        return localizeToMap(k -> k, CUSTOM_PATH);
    }
}
