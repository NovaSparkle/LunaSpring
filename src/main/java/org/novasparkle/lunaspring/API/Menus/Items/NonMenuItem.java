package org.novasparkle.lunaspring.API.Menus.Items;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.Util.utilities.Utils;
import org.novasparkle.lunaspring.API.Util.Service.managers.ColorManager;
import org.novasparkle.lunaspring.API.Util.Service.managers.NBTManager;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class NonMenuItem {
    @Setter
    private ItemStack itemStack;
    private final String id = Utils.getRKey((byte) 14);
    private Material material;
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private int amount = 1;
    private boolean glowing = false;
    private String headValue;

    public NonMenuItem(Material material, String displayName, List<String> lore, int amount) {
        this.material = material;
        this.displayName = ColorManager.color(displayName);
        if (!lore.isEmpty())
            lore.replaceAll(ColorManager::color);

        this.lore = lore;
        this.amount = amount;
        if (this.amount < 1) this.amount = 1;
        this.itemStack = new ItemStack(this.material, this.amount);
        this.update();
    }

    public NonMenuItem(Material material) {
        this.material = material;
        this.itemStack = new ItemStack(this.material, this.amount);
        this.update();
    }

    public NonMenuItem(Material material, int amount) {
        this.material = material;
        this.amount = amount;
        if (this.amount < 1) this.amount = 1;
        this.itemStack = new ItemStack(this.material, this.amount);
        this.update();
    }

    public NonMenuItem(ConfigurationSection section) {
        String material = section.getString("material");
        assert material != null;
        this.material = Material.getMaterial(material);

        List<String> lore = section.getStringList("lore");
        String displayName = section.getString("displayName");

        this.displayName = ColorManager.color(displayName);

        if (!lore.isEmpty()) lore.replaceAll(ColorManager::color);
        this.lore = lore;

        this.amount = section.getInt("amount");
        if (this.amount < 1) this.amount = 1;

        this.itemStack = new ItemStack(this.material, this.amount);

        this.setGlowing(section.getBoolean("enchanted"));
        ConfigurationSection nbtSection = section.getConfigurationSection("nbtTags");
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


        this.update();

        String baseHeadValue = section.getString("baseHead");
        if (baseHeadValue != null && !baseHeadValue.isEmpty()) {
            this.headValue = baseHeadValue;
            this.applyBaseHead(baseHeadValue);
        }
    }

    public void setMaterial(Material material) {
        this.material = material;
        this.update();
    }

    public void setAmount(int amount) {
        this.amount = amount;
        this.update();
    }

    public void setDisplayName(String displayName) {
        this.displayName = ColorManager.color(displayName);
        this.update();
    }

    public void setLore(List<String> lore) {
        lore.replaceAll(ColorManager::color);
        this.lore = lore;
        this.update();
    }

    public void setGlowing(boolean enchanted) {
        this.glowing = enchanted;
        if (enchanted) {
            this.itemStack.addUnsafeEnchantment(Enchantment.LUCK, 1);
            this.itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            this.itemStack.removeEnchantment(Enchantment.LUCK);
            this.itemStack.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    @Override
    public String toString() {
        return "NonMenuItem{" +
                "id='" + id + '\'' +
                ", material=" + material +
                ", displayName='" + displayName + '\'' +
                ", lore=" + lore +
                ", amount=" + amount +
                '}';
    }

    public void serialize(@NotNull ConfigurationSection section, boolean asItemStack) {
        if (asItemStack)
            section.set("item", this.itemStack);
        else {
            section.set("material", this.getMaterial().name());
            section.set("amount", this.getAmount());
            section.set("displayName", this.getDisplayName());
            section.set("lore", this.getLore());
            section.set("enchanted", this.glowing);
            section.set("headValue", this.headValue);
            section.set("id", this.id);
        }
    }

    public void setAll(Material material, int amount, String displayName, List<String> lore, boolean enchanted) {
        if (material != null)
            this.setMaterial(material);
        if (amount > 0)
            this.setAmount(amount);
        if (lore != null && !lore.isEmpty())
            this.setLore(lore);
        if (displayName != null && !displayName.isEmpty())
            this.setDisplayName(displayName);
        this.setGlowing(enchanted);
    }

    public void setAll(ConfigurationSection itemSection) {
        if (itemSection == null) return;

        String strMaterial = itemSection.getString("material");
        Material newMaterial = strMaterial == null || strMaterial.isEmpty() ? null : Material.getMaterial(strMaterial);

        int amount = itemSection.getInt("amount");
        String displayName = itemSection.getString("displayName");
        List<String> lore = new ArrayList<>(itemSection.getStringList("lore"));

        this.setAll(newMaterial, amount, displayName, lore, itemSection.getBoolean("enchanted"));
    }

    @SuppressWarnings("deprecation")
    private void update() {
        this.itemStack.setType(this.material);
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            if (this.displayName != null && !this.displayName.isEmpty())
                meta.setDisplayName(this.displayName);
            if (this.lore != null && !this.lore.isEmpty())
                meta.setLore(this.lore);
            this.itemStack.setItemMeta(meta);
        }
        this.itemStack.setAmount(this.amount);

        if (!NBTManager.hasTag(this.itemStack, "lunaspring.itemId")) {
            NBTManager.setString(this.itemStack, "lunaspring-itemId", this.id);
        }
    }

    public void applyNBT(Map<String, String> nbtTags) {
        nbtTags.forEach((key, value) ->
                NBTManager.setString(this.itemStack, key, value));
    }

    public void applyBaseHead(String value) {
        NBTManager.base64head(this.itemStack, value);
        this.headValue = value;
    }

    public void applyBaseHead(OfflinePlayer player) {
        this.setItemStack(NBTManager.base64head(this.itemStack, player));
    }

    public boolean checkId(String id) {
        return Objects.equals(id, this.id);
    }

    public boolean checkId(ItemStack item) {
        String id = NBTManager.getString(item, "lunaspring-item-id");
        return id != null && !id.isEmpty() && this.checkId(id);
    }

    @SuppressWarnings("deprecation")
    public ItemStack getDefaultStack() {
        ItemStack item = new ItemStack(this.material, this.amount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.displayName);
        meta.setLore(this.lore);
        item.setItemMeta(meta);

        if (this.headValue != null) NBTManager.base64head(item, this.headValue);
        return itemStack;
    }
    public void give(Player player) {
        this.lore.forEach(lr -> PlaceholderAPI.setPlaceholders(player, lr));
        player.getInventory().addItem(this.itemStack);
    }
}