package org.novasparkle.lunaspring.API.menus.items;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.novasparkle.lunaspring.API.util.exceptions.NoItemMetaException;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.service.managers.NBTManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.io.Serializable;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Getter
@SuppressWarnings({"unused", "deprecation"})
@Accessors(chain = true, fluent = false)
public class NonMenuItem implements Cloneable {
    @Setter private ItemStack itemStack;
    @Setter private String id = Utils.getRKey((byte) 14);
    protected Material material;
    protected String displayName;
    protected List<String> lore;
    @Range(from = 1, to = 64)
    protected int amount;
    protected boolean glowing = false;
    protected String headValue;
    protected Map<Enchantment, Integer> enchantments = Maps.newHashMap();
    protected List<ItemFlag> itemFlags = Lists.newArrayList();

    @Builder(builderMethodName = "superbuilder", buildMethodName = "superbuild")
    public NonMenuItem(Material material, String displayName, List<String> lore, int amount) {
        if (material == null) material = Material.STONE;
        this.material = material;
        this.amount = Math.max(amount, 1);
        this.itemStack = new ItemStack(this.material, this.amount);
        this.displayName = ColorManager.color(displayName);
        this.setLore(lore);
    }

    public NonMenuItem() {
        this(Material.STONE);
    }

    public NonMenuItem(Material material) {
        this(material, null, Lists.newArrayList(), 1);
    }

    public NonMenuItem(Material material, int amount) {
        this(material, null, Lists.newArrayList(), amount);
    }

    public NonMenuItem(@NotNull ConfigurationSection section) {
        this(Utils.getMaterial(section.getString("material")),
                section.getString("displayName"),
                section.getStringList("lore"),
                section.getInt("amount"));

        this.setGlowing(section.getBoolean("enchanted"));

        // Enchantments
        this.applyEnchantments(section);

        // ItemFlags
        this.applyItemFlags(section);

        // NBT
        ConfigurationSection nbtSection = section.getConfigurationSection("NBT");
        this.applyNBT(nbtSection);

        // Head
        this.applyBaseHead(section);

        // ATTRIBUTES
        this.applyAttributes(section);

        // META COLOR
        this.setMetaColor(section.getString("color"));

        // DURABILITY
        this.setDurability(section);
    }

    // SETTERS

    public NonMenuItem setMaterial(@NotNull Material material) {
        this.material = material;
        this.update();
        return this;
    }

    public NonMenuItem setAmount(int amount) {
        this.amount = Math.max(amount, 1);
        this.update();
        return this;
    }

    public NonMenuItem increase() {
        return this.setAmount(this.amount + 1);
    }

    public NonMenuItem decrease() {
        return this.setAmount(this.getAmount() - 1);
    }

    public NonMenuItem addAmount(int add) {
        return this.setAmount(this.getAmount() + add);
    }

    public NonMenuItem setDisplayName(String displayName, String... replacements) {
        this.displayName = ColorManager.color(Utils.applyReplacements(displayName, replacements));
        this.update();
        return this;
    }

    public NonMenuItem setLore(List<String> lore, String... replacements) {
        this.lore = lore;
        this.replaceLore(l -> ColorManager.color(Utils.applyReplacements(l, replacements)));
        return this;
    }

    public NonMenuItem setGlowing(boolean enchanted) {
        this.glowing = enchanted;
        if (enchanted) {
            this.itemStack.addUnsafeEnchantment(Enchantment.LUCK, 1);
            this.itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            this.itemStack.removeEnchantment(Enchantment.LUCK);
            this.itemStack.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public NonMenuItem setAll(Material material, int amount, String displayName, List<String> lore, boolean enchanted) {
        if (material != null)
            this.setMaterial(material);
        if (amount > 0)
            this.setAmount(amount);
        if (lore != null && !lore.isEmpty())
            this.setLore(lore);
        if (displayName != null && !displayName.isEmpty())
            this.setDisplayName(displayName);
        this.setGlowing(enchanted);
        return this;
    }

    public NonMenuItem setAll(@NotNull ConfigurationSection itemSection) {
        String strMaterial = itemSection.getString("material");
        Material newMaterial = strMaterial == null || strMaterial.isEmpty() ? null : Material.valueOf(strMaterial);

        int amount = itemSection.getInt("amount");
        String displayName = itemSection.getString("displayName");
        List<String> lore = new ArrayList<>(itemSection.getStringList("lore"));
        this.itemFlags.clear();
        this.enchantments.clear();
        this.applyBaseHead(itemSection);
        this.applyEnchantments(itemSection);
        this.applyItemFlags(itemSection);
        this.applyAttributes(itemSection);
        this.setMetaColor(itemSection.getString("color"));
        this.setDurability(itemSection);

        this.setAll(newMaterial, amount, displayName, lore, itemSection.getBoolean("enchanted"));
        return this;
    }

    public NonMenuItem update() throws NoItemMetaException {
        this.itemStack.setType(this.material);
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta == null)
            throw new NoItemMetaException(this.itemStack);

        if (this.displayName != null && !this.displayName.isEmpty())
            meta.setDisplayName(this.displayName);

        if (this.lore != null && !this.lore.isEmpty())
            meta.setLore(new ArrayList<>(this.lore));


        this.itemStack.setItemMeta(meta);
        this.itemStack.setAmount(this.amount);
        return this;
    }

    public NonMenuItem replaceLore(UnaryOperator<String> operator) {
        this.getLore().replaceAll(operator);
        this.update();
        return this;
    }

    public EquipmentSlot getEquipmentSlot() {
        return Utils.getEquipmentSlot(this.material);
    }

    // APPLIERS - NBT, ItemFlags, Attributes, Enchantments, BaseHeads

    public NonMenuItem applyNBT(Map<String, String> nbtTags) {
        nbtTags.forEach((key, value) -> NBTManager.setString(this.itemStack, key, value));
        return this;
    }

    public NonMenuItem applyNBT(ConfigurationSection nbtSection) {
        if (nbtSection != null) {
            nbtSection.getValues(false).forEach((key, value) -> {
                if (!NBTManager.hasTag(this.itemStack, key)) {
                    if (value instanceof String strValue) NBTManager.setString(this.itemStack, key, strValue);

                    else if (value instanceof Integer intValue) NBTManager.setInt(this.itemStack, key, intValue);

                    else if (value instanceof Boolean boolValue) NBTManager.setBool(this.itemStack, key, boolValue);

                    else if (value instanceof Double dValue) NBTManager.setDouble(itemStack, key, dValue);
                }
            });
        }
        return this;
    }

    public NonMenuItem applyItemFlags(ConfigurationSection section) {
        this.applyItemFlags(section.getStringList("itemflags").stream().map(ItemFlag::valueOf).collect(Collectors.toSet()));
        return this;
    }

    public NonMenuItem applyItemFlags(Collection<ItemFlag> itemFlags) {
        return this.applyItemFlags(itemFlags.toArray(new ItemFlag[0]));
    }

    public NonMenuItem applyItemFlags(ItemFlag... itemFlags) throws NoItemMetaException {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta == null) throw new NoItemMetaException(this.itemStack);
        if (itemFlags != null) {
            this.itemFlags.addAll(List.of(itemFlags));
            meta.addItemFlags(itemFlags);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }


    public NonMenuItem applyBaseHead(ConfigurationSection section) {
        String baseHeadValue = section.getString("baseHead");
        if (baseHeadValue != null && !baseHeadValue.isEmpty()) {
            if (!this.material.equals(Material.PLAYER_HEAD)) {
                this.setMaterial(Material.PLAYER_HEAD);
            }
            this.headValue = baseHeadValue;
            NBTManager.base64head(this.itemStack, baseHeadValue);
        }
        return this;
    }

    public NonMenuItem applyBaseHead(@NotNull OfflinePlayer player) {
        NBTManager.base64head(this.itemStack, player);
        return this;
    }

    public NonMenuItem applyEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public NonMenuItem applyEnchantments(Map<Enchantment, Integer> enchants) {
        if (enchants != null) {
            this.enchantments.putAll(enchants);
            this.itemStack.addUnsafeEnchantments(enchants);
        }
        return this;
    }

    public NonMenuItem applyEnchantments(ConfigurationSection section) {
        ConfigurationSection eSection = section.getConfigurationSection("enchants");
        if (eSection != null)
            eSection.getValues(false).forEach((enchant, level) -> {
                        Enchantment enchantment = new EnchantmentWrapper(enchant);
                        this.enchantments.put(enchantment, (Integer) level);
                        this.itemStack.addUnsafeEnchantment(enchantment, (Integer) level);
                    });
        return this;
    }

    public NonMenuItem applyAttributes(ConfigurationSection section) {
        ConfigurationSection aSection = section.getConfigurationSection("attributes");
        if (aSection == null) return this;

        // attributes:
        //   GENERIC_ARMOR:
        //     operation: ADD_NUMBER
        //     slot: CHESTPLATE
        //     value: 0.4

        for (String key : aSection.getKeys(false)) {
            Attribute attribute = Attribute.valueOf(key);

            String stringOperation = aSection.getString(key + ".operation");
            AttributeModifier.Operation operation = Utils.getEnumValue(AttributeModifier.Operation.class, stringOperation, AttributeModifier.Operation.ADD_NUMBER);

            double value = aSection.getDouble(key + ".value");
            EquipmentSlot slot = Utils.getEnumValue(EquipmentSlot.class, aSection.getString(key + ".slot"));
            this.addAttribute(attribute, new AttributeModifier(UUID.randomUUID(), Utils.getRKey((byte) 12), value, operation, slot));
        }
        return this;
    }

    public NonMenuItem setDurability(int amount, boolean isSpentMode) {
        this.getItemStack().setDurability((short) (isSpentMode ? amount : this.material.getMaxDurability() - amount));
        return this;
    }

    public NonMenuItem setDurability(ConfigurationSection section) {
        ConfigurationSection durabilitySection = section.getConfigurationSection("durability");
        if (durabilitySection == null) {
            int durability = section.getInt("durability", -1);
            return durability >= 0 ? this.setDurability(durability, false) : this;
        }

        int durability = durabilitySection.getInt("amount");
        boolean isSpentMode = durabilitySection.getBoolean("spent");
        return this.setDurability(durability, isSpentMode);
    }

    public int getDurability() {
        return this.material.getMaxDurability() - this.getItemStack().getDurability();
    }

    public NonMenuItem setMetaColor(Color color) {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta instanceof LeatherArmorMeta colorMeta) {
            colorMeta.setColor(color);
            return this;
        }

        if (meta instanceof PotionMeta colorMeta) {
            colorMeta.setColor(color);
            return this;
        }

        if (meta instanceof MapMeta colorMeta) {
            colorMeta.setColor(color);
            return this;
        }

        return this;
    }

    public NonMenuItem setMetaColor(int red, int green, int blue) {
        return this.setMetaColor(Color.fromRGB(red, green, blue));
    }

    public NonMenuItem setMetaColor(int rgb) {
        return this.setMetaColor(Color.fromRGB(rgb));
    }

    // red;green;blue
    public NonMenuItem setMetaColor(String rgbDelimiter) {
        if (rgbDelimiter == null || rgbDelimiter.isEmpty()) return this;
        String[] split = rgbDelimiter.split(";");
        if (split.length < 3) return this;
        else return this.setMetaColor(LunaMath.toInt(split[0]), LunaMath.toInt(split[1]), LunaMath.toInt(split[2]));
    }

    public NonMenuItem addAttribute(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) throws NoItemMetaException {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta == null) throw new NoItemMetaException(this.itemStack);
        meta.addAttributeModifier(attribute, modifier);

        this.itemStack.setItemMeta(meta);
        return this;
    }

    public void removeAttribute(Attribute attribute, AttributeModifier.Operation operation, double checkedAmount, boolean removeAll) throws NoItemMetaException {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta == null) throw new NoItemMetaException(this.itemStack);

        Collection<AttributeModifier> modifierMap = meta.getAttributeModifiers(attribute);
        if (modifierMap == null || modifierMap.isEmpty()) return;

        for (AttributeModifier modifier : modifierMap) {
            if (modifier.getOperation() != operation || modifier.getAmount() != checkedAmount) continue;

            meta.removeAttributeModifier(attribute, modifier);
            if (!removeAll) break;
        }
        this.itemStack.setItemMeta(meta);
    }

    // CHECKS

    /**
     * Cравнение с без учета кол-ва предмета
     */
    public boolean isSimilar(ItemStack itemStack) {
        if (itemStack == this.itemStack) return true;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return false;
        return this.getMaterial().equals(itemStack.getType()) &&
                this.getLore().equals(meta.getLore()) &&
                this.getDisplayName().equals(meta.getDisplayName()) &&
                NBTManager.isSimilar(this.itemStack, itemStack);
    }

    /**
     * Cравнение с учетом кол-ва предмета
     *
     */
    @Override
    public boolean equals(Object item) {
        if (this == item) return true;
        if (item == null || this.getClass() != item.getClass()) return false;
        NonMenuItem that = (NonMenuItem) item;
        return this.isSimilar(that.itemStack) && that.getAmount() == this.getAmount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemStack(), getId(), getMaterial(), getDisplayName(), getLore(), getAmount(), isGlowing(), getHeadValue());
    }

    @Override
    public String toString() {
        return "NonMenuItem{" +
                "itemStack=" + itemStack +
                ", id='" + id + '\'' +
                ", material=" + material +
                ", displayName='" + displayName + '\'' +
                ", lore=" + lore +
                ", amount=" + amount +
                ", glowing=" + glowing +
                ", headValue='" + headValue + '\'' +
                ", enchantments=" + enchantments +
                ", itemFlags=" + itemFlags +
                '}';
    }



    // ACTIONS

    public Item drop(Location location) {
        return location.getWorld().dropItem(location, this.getItemStack());
    }

    public Item dropNaturally(Location location) {
        return location.getWorld().dropItemNaturally(location, this.getItemStack());
    }

    public void give(@NotNull Player player) {
        this.replaceLore(lr -> PlaceholderAPI.setPlaceholders(player, lr));
        Utils.Items.give(player, false, this.itemStack);
    }

    public NonMenuItem serialize(@NotNull ConfigurationSection section, boolean asItemStack) {
        if (asItemStack)
            section.set("item", this.itemStack);
        else {
            section.set("material", this.getMaterial().name());
            section.set("amount", this.getAmount());
            section.set("displayName", this.getDisplayName());
            section.set("lore", this.getLore());
            section.set("enchanted", this.glowing);
            section.set("headValue", this.headValue);
            section.set("enchants", this.enchantments);
            section.set("flags", this.itemFlags);
            section.set("id", this.id);
        }
        return this;
    }

    public static NonMenuItem fromItemStack(ItemStack stack) {
        NonMenuItem nonMenuItem = new NonMenuItem(stack.getType(), stack.getAmount());
        ItemMeta meta = nonMenuItem.itemStack.getItemMeta();
        if (meta != null) {
            nonMenuItem.displayName = meta.getDisplayName();

            List<String> lore = meta.getLore();
            if (lore != null && !lore.isEmpty()) nonMenuItem.lore = lore;

            if (meta.hasEnchants()) nonMenuItem.glowing = true;

            nonMenuItem.itemFlags = new ArrayList<>(meta.getItemFlags());
        }

        nonMenuItem.itemStack = stack;
        return nonMenuItem.update();
    }

    @Override
    public NonMenuItem clone() throws CloneNotSupportedException {
        NonMenuItem copy = (NonMenuItem) super.clone();
        copy.itemStack = this.itemStack.clone();
        copy.lore = new ArrayList<>(this.lore);
        copy.enchantments = new HashMap<>(this.enchantments);
        copy.itemFlags = new ArrayList<>(this.itemFlags);
        return copy;
    }
}