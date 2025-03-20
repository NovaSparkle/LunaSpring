package org.novasparkle.lunaspring.Menus.Items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Menus.IMenu;
import org.novasparkle.lunaspring.Util.Utils;
import org.novasparkle.lunaspring.other.NonMenuItem;

import java.util.List;

@Getter
public class Item extends NonMenuItem {
    private IMenu menu;
    @Setter private byte slot = 0;

    public Item(Material material, String displayName, List<String> lore, int amount, byte slot) {
        super(material, displayName, lore, amount);
        this.slot = slot;
    }

    public Item(Material material) {
        super(material);
    }

    public Item(Material material, int amount) {
        super(material, amount);
    }

    public Item(ConfigurationSection section, int slot) {
        super(section);
        this.slot = (byte) slot;
    }

    public Item(ConfigurationSection section, boolean rowCol) {
        super(section);
        if (rowCol)
            this.slot = (byte) Utils.getIndex(section.getInt("slot.row"), section.getInt("slot.column"));
        else this.slot = (byte) section.getInt("slot");
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + this.getId() + '\'' +
                ", material=" + this.getMaterial() +
                ", displayName='" + this.getDisplayName() + '\'' +
                ", lore=" + this.getLore() +
                ", amount=" + this.getAmount() +
                ", slot=" + this.slot +
                '}';
    }

    public void insert(IMenu aMenu) {
        this.menu = aMenu;
        aMenu.getInventory().setItem(this.slot, this.getItemStack());
    }

    public void insert(IMenu aMenu, byte slot) {
        this.menu = aMenu;
        this.slot = slot;
        aMenu.getInventory().setItem(slot, this.getItemStack());
    }

    public void insert(IMenu aMenu, byte row, byte column) {
        this.menu = aMenu;
        this.slot = (byte) Utils.getIndex(row, column);
        aMenu.getInventory().setItem(slot, this.getItemStack());
    }

    public void remove(IMenu iMenu) {
        iMenu.getInventory().setItem(this.slot, null);
    }
}
