package org.novasparkle.lunaspring.Menus.Items.buttons;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Menus.Items.Item;

import java.util.List;

public abstract class Button extends Item implements IButton {
    public Button(Material material, String displayName, List<String> lore, int amount, byte slot) {
        super(material, displayName, lore, amount, slot);
    }

    public Button(ConfigurationSection section, boolean rowCol) {
        super(section, rowCol);
    }

    public Button(ConfigurationSection section, int slot) {
        super(section, slot);
    }
}
