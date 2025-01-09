package org.novasparkle.lunaspring.Menus.Items;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Menus.IMenu;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Decoration {
    private final List<Item> decorationItems = new ArrayList<>();
    public Decoration(ConfigurationSection decorationSection) {
        for (String key : decorationSection.getKeys(false)) {
            ConfigurationSection itemSection = decorationSection.getConfigurationSection(key);
            assert itemSection != null;

            List<String> slots = itemSection.getStringList("slots");
            if (!slots.isEmpty()) {
                slots.forEach(unsplitedSlots -> {
                    String[] splitedSlots = unsplitedSlots.split("-");
                    if (splitedSlots.length == 1) {
                        this.decorationItems.add(new Item(itemSection, Byte.parseByte(splitedSlots[0])));
                    }
                    else if (splitedSlots.length >= 2) {
                        for (byte slot = Byte.parseByte(splitedSlots[0]); slot < Byte.parseByte(splitedSlots[1]); slot++) {
                            this.decorationItems.add(new Item(itemSection, slot));
                        }
                    }
                });
            }
        }
    }

    public void insert(IMenu imenu) {
        this.decorationItems.forEach(i -> i.insert(imenu.getInventory()));
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
}
