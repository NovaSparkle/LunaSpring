package org.novasparkle.lunaspring.Menus;

import org.novasparkle.lunaspring.Items.Item;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Decoration {

    private final List<Item> decorationItems = new ArrayList<>();


    public Decoration(ConfigurationSection decorationSection) {
        for (String key : decorationSection.getKeys(false)) {

            ConfigurationSection itemSection = decorationSection.getConfigurationSection(key);
            assert itemSection != null;

            List<String> slotsStr = itemSection.getStringList("slotsStr");

            if (slotsStr.isEmpty()) {
                List<Integer> slotsInt = itemSection.getIntegerList("slotsInt");

                for (int slot : slotsInt) {
                    this.decorationItems.add(new Item(itemSection, slot));
                }

            } else {
                for (String slotStr : slotsStr) {
                    String[] strArray = slotStr.split("-");

                    for (int slot = Integer.parseInt(strArray[0]); slot < Integer.parseInt(strArray[1]); slot++) {
                        this.decorationItems.add(new Item(itemSection, slot));
                    }
                }
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
}
