package org.novasparkle.lunaspring.API.menus.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @SuppressWarnings("unused")
public class Decoration {
    private final List<Item> decorationItems;
    private final IMenu iMenu;


    public Decoration(ConfigurationSection decorationSection, IMenu iMenu) {
        this.iMenu = iMenu;
        this.decorationItems = new ArrayList<>();
        boolean fillType = decorationSection.getBoolean("fillType.enabled");

        if (fillType) {
            ConfigurationSection section = decorationSection.getConfigurationSection("fillType.item");
            Inventory inventory = this.iMenu.getInventory();
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {
                    this.decorationItems.add(new Item(section, i));
                }
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
        this.decorationItems.forEach(i -> i.insert(this.iMenu));
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
