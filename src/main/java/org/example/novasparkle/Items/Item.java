package org.example.novasparkle.Items;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Item {

    private final ItemStack itemStack;

    private Material material;

    private String displayName;

    private List<String> lore = new ArrayList<>();

    private int amount;

    private byte slot;


    public Item(Material material, String displayName, List<String> lore, int amount, byte slot) {
        this.material = material;
        this.slot = slot;
        this.displayName = displayName;
        this.lore = lore;
        this.amount = amount;
        this.itemStack = new ItemStack(this.material, this.amount);
    }
    public Item(Material material) {
        this.material = material;
        this.amount = 1;
        this.itemStack = new ItemStack(this.material, this.amount);
    }
    public Item(Material material, int amount) {
        this.material = material;
        this.amount = amount;
        this.itemStack = new ItemStack(this.material, this.amount);
    }
    public Item(ConfigurationSection section) {
        String material = section.getString("material");
        assert material != null;
        this.material = Material.getMaterial(material);
        this.displayName = section.getString("displayName");
        this.lore = section.getStringList("lore");
        this.slot = (byte) section.getInt("slot");
        this.amount = section.getInt("amount");
        this.itemStack = new ItemStack(this.material, this.amount);
    }
    public Item(ConfigurationSection section, int slot) {
        String material = section.getString("material");
        assert material != null;
        this.material = Material.getMaterial(material);
        this.displayName = section.getString("displayName");
        this.lore = section.getStringList("lore");
        this.slot = (byte) slot;
        this.amount = section.getInt("amount");
        this.itemStack = new ItemStack(this.material, this.amount);
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
        this.displayName = displayName;
        this.update();
    }
    public void setLore(List<String> lore) {
        this.lore = lore;
        this.update();
    }
    public void setGlowing() {
        this.itemStack.addUnsafeEnchantment(Enchantment.LUCK, 1);
        this.itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }
    public void setAll(Material material, int amount, String displayName, List<String> lore, boolean enchanted) {
        this.setMaterial(material);
        this.setAmount(amount);
        this.setLore(lore);
        this.setDisplayName(displayName);
        if (enchanted) this.setGlowing();
    }
    @SuppressWarnings("deprecation")
    private void update() {
        this.itemStack.setType(this.material);
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(this.displayName);
        meta.setLore(this.lore);
        this.itemStack.setAmount(this.amount);
        this.itemStack.setItemMeta(meta);
    }


    public void insert(Inventory inventory) {
        inventory.setItem(this.slot, this.itemStack);
    }
    public void insert(Inventory inventory, byte slot) {
        this.slot = slot;
        inventory.setItem(slot, this.itemStack);
    }


    public void remove(Inventory inventory) {
        inventory.setItem(this.slot, null);
    }
}
