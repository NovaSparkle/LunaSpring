package org.novasparkle.lunaspring.Menus.Items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Menus.IMenu;
import org.novasparkle.lunaspring.Util.Utils;
import org.novasparkle.lunaspring.other.NonMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter @Setter
public class Decoration {
    private final List<Item> decorationItems = new ArrayList<>();

    @Override
    public String toString() {
        return "Decoration{" +
                "decorationItems=" + this.decorationItems +
                '}';
    }

    public Decoration(ConfigurationSection decorationSection) {
        for (String key : decorationSection.getKeys(false)) {
            ConfigurationSection itemSection = decorationSection.getConfigurationSection(key);
            assert itemSection != null;

            List<String> slots = itemSection.getStringList("slots");
            this.decorationItems.addAll(Utils.getItems(itemSection, slots));
        }
    }

    public void insert(IMenu imenu) {
        this.decorationItems.forEach(i -> i.insert(imenu));
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

    public boolean checkItemId(String id) {
        return this.decorationItems.stream().anyMatch(i -> i.checkId(id));
    }

    public boolean checkItemId(ItemStack item) {
        return this.decorationItems.stream().anyMatch(i -> i.checkId(item));
    }
}
