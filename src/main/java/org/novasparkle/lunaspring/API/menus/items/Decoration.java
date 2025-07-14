package org.novasparkle.lunaspring.API.menus.items;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter @SuppressWarnings("unused")
public class Decoration implements Cloneable {
    private final Inventory inventory;
    private final List<Item> decorationItems;
    public Decoration(ConfigurationSection decorationSection, Inventory inventory) {
        this.inventory = inventory;
        this.decorationItems = new ArrayList<>();
        boolean fillType = decorationSection.getBoolean("fillType.enabled");

        if (fillType) {
            ConfigurationSection section = decorationSection.getConfigurationSection("fillType.item");

            assert section != null;
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) this.decorationItems.add(new Item(section, i));
            }
        } else {
            for (String key : decorationSection.getKeys(false)) {
                ConfigurationSection itemSection = decorationSection.getConfigurationSection(key);
                assert itemSection != null;

                List<String> slots = itemSection.getStringList("slots");
                this.decorationItems.addAll(Utils.getItems(itemSection, slots));
            }
        }
    }

    @Override
    public String toString() {
        return "Decoration{" +
                "decorationItems=" + this.decorationItems +
                '}';
    }

    public void insert() {
        this.decorationItems.forEach(i -> this.inventory.setItem(i.getSlot(), i.getItemStack()));
    }

    public int getDecorationsAmount() {
        return this.decorationItems.size();
    }

    public boolean checkSlot(byte slot) {
        return this.decorationItems.stream().anyMatch(i -> i.getSlot() == slot);
    }

    public boolean checkMaterial(Material material) {
        return this.decorationItems.stream().anyMatch(i -> i.getMaterial().equals(material));
    }

    public boolean checkItemStack(ItemStack itemStack) {
        return this.decorationItems.stream().anyMatch(i -> i.getItemStack().equals(itemStack));
    }

    public boolean checkAll(ItemStack itemStack, byte slot) {
        return this.decorationItems.stream().anyMatch(i -> i.getItemStack().equals(itemStack) && i.getSlot() == slot);
    }

    public boolean checkAll(Material material, byte slot) {
        return this.decorationItems.stream().anyMatch(i -> i.getMaterial() == material && i.getSlot() == slot);
    }

    @Override
    @SneakyThrows
    public Decoration clone() {
        return (Decoration) super.clone();
    }
}
