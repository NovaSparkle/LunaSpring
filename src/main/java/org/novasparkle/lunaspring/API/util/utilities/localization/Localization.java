package org.novasparkle.lunaspring.API.util.utilities.localization;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.self.configuration.LSConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class Localization {
    @Getter
    private final Map<Class<?>, Function<Object, @NotNull String>> functions = new HashMap<>();
    @Getter
    private ILang selectedLanguage;

    static {
        functions.put(Attribute.class, o -> "attribute.name." + ((Attribute) o).getKey().getKey());
        functions.put(Material.class, o -> {
            Material material = (Material) o;
            String path = material.isBlock() ? "block" : "item";
            var key = material.getKey();
            return path + "." + key.getNamespace() + "." + key.getKey();
        });
        functions.put(EntityType.class, o -> {
            EntityType entityType = (EntityType) o;
            return "entity.minecraft." + entityType.getKey().getKey();
        });
        functions.put(PotionEffectType.class, o -> {
            PotionEffectType potionEffectType = (PotionEffectType) o;
            return "effect.minecraft." + potionEffectType.getName().toLowerCase();
        });
        functions.put(GameRule.class, o -> {
            GameRule<?> gr = (GameRule<?>) o;
            return "gamerule." + gr.getName();
        });
        functions.put(Entity.class, o -> {
            Entity entityType = (Entity) o;
            return functions.get(EntityType.class).apply(entityType.getType());
        });
        functions.put(ItemStack.class, o -> {
            ItemStack itemStack = (ItemStack) o;
            return functions.get(Material.class).apply(itemStack.getType());
        });
        functions.put(Block.class, o -> {
            Block block = (Block) o;
            return functions.get(Material.class).apply(block.getType());
        });
        functions.put(Keyed.class, o -> {
            var key = ((Keyed) o).getKey();
            String clName = o.getClass().getSimpleName().toLowerCase();
            return clName + "." + key.getNamespace() + "." + key.getKey();
        });
        functions.put(EnchantmentWrapper.class, o -> {
            var key = ((Keyed) o).getKey();
            return "enchantment." + key.getNamespace() + "." + key.getKey();
        });
        functions.put(PotionEffectTypeWrapper.class, o -> {
            return functions.get(PotionEffectType.class).apply(o);
        });
        functions.put(PotionEffect.class, o -> {
            return functions.get(PotionEffectType.class).apply(((PotionEffect) o).getType());
        });
    }

    public void load() {
        String lang = LSConfig.getString("language");
        if (lang == null || lang.isEmpty()) return;

        selectedLanguage = new LunaLang(lang);
    }

    public boolean isLoad() {
        return selectedLanguage != null;
    }

    @Nullable
    public String translate(String path, @Nullable Supplier<String> orElse) {
        return isLoad() ? selectedLanguage.translate(path, orElse) : null;
    }

    @Nullable
    public String translate(Object object, @Nullable Supplier<String> orElse) {
        var func = functions.get(object.getClass());
        if (func == null && object instanceof Keyed k) {
            func = functions.get(Keyed.class);
        }

        if (func == null) return orElse == null ? null : orElse.get();
        return translate(func.apply(object), orElse);
    }

    @Nullable
    public String translate(Object value) {
        return translate(value, null);
    }

    @Nullable
    public String getPath(String value) {
        return isLoad() ? selectedLanguage.getPath(value) : null;
    }

    @Nullable
    public <E> E detranslate(String translatedValue, Function<String[], E> func) {
        return isLoad() ? selectedLanguage.detranslate(translatedValue, func) : null;
    }

    @Nullable
    public Attribute detranslateAttribute(String translatedValue) {
        return detranslate(translatedValue, s -> Registry.ATTRIBUTE.get(
                NamespacedKey.minecraft(s[s.length - 2] + "." + s[s.length - 1])
        ));
    }

    @Nullable
    public Material detranslateMaterial(String translatedValue) {
        return detranslate(translatedValue, s -> Registry.MATERIAL.get(
                NamespacedKey.minecraft(s[s.length - 1])
        ));
    }

    @Nullable
    public PotionEffectType detranslateEffectType(String translatedValue) {
        return detranslate(translatedValue, s -> PotionEffectType.getByName(s[s.length - 1]));
    }

    @Nullable
    public EntityType detranslateEntityType(String translatedValue) {
        return detranslate(translatedValue, s -> Registry.ENTITY_TYPE.get(
                NamespacedKey.minecraft(s[s.length - 1])
        ));
    }

    @Nullable
    public GameRule<?> detranslateGamerule(String translatedValue) {
        return detranslate(translatedValue, s -> GameRule.getByName(s[s.length - 1]));
    }

    @Nullable
    public <E extends Enum<E>> E detranslateEnum(String translatedValue, Class<E> clazz) {
        return detranslate(translatedValue, s -> {
            String name = s[s.length - 1].toUpperCase();
            return Utils.getEnumValue(clazz, name);
        });
    }

    @Nullable
    public Enchantment detranslateEnchantment(String translatedValue) {
        return detranslate(translatedValue, s ->
            Registry.ENCHANTMENT.get(NamespacedKey.minecraft(s[s.length - 1]))
        );
    }

    @Nullable
    public Map<String, String> getReversedTranslations() {
        return isLoad() ? selectedLanguage.getReversedTranslations() : null;
    }
}
