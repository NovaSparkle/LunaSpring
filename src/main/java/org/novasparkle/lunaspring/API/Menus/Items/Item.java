package org.novasparkle.lunaspring.API.Menus.Items;

import com.ibm.icu.impl.PVecToTrieCompactHandler;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.Menus.IMenu;
import org.novasparkle.lunaspring.API.Util.utilities.LunaMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

@Getter
public class Item extends NonMenuItem {
    private final List<String> defaultLore;
    private final String defaultName;

    private IMenu menu;
    @Setter private byte slot = 0;

    public Item(Material material, String displayName, List<String> lore, int amount, byte slot) {
        super(material, displayName, lore, amount);
        this.slot = slot;
        this.defaultLore = new ArrayList<>(this.getLore());
        this.defaultName = this.getDisplayName();
    }

    public Item(Material material, int amount) {
        super(material, amount);
        this.defaultLore = new ArrayList<>(this.getLore());
        this.defaultName = this.getDisplayName();
    }

    public Item(NonMenuItem nonMenuItem, byte slot) {
        this(nonMenuItem.getMaterial(), nonMenuItem.getDisplayName(), nonMenuItem.getLore(), nonMenuItem.getAmount(), slot);
    }

    public Item(Material material) {
        this(material, 1);
    }

    public Item(Material material, byte slot) {
        this(material);
        this.slot = slot;
    }

    public Item(ConfigurationSection section, int slot) {
        super(section);
        this.slot = (byte) slot;
        this.defaultLore = new ArrayList<>(this.getLore());
        this.defaultName = this.getDisplayName();
    }

    public Item(ConfigurationSection section, boolean rowCol) {
        this(section, 0);
        if (rowCol)
            this.slot = (byte) LunaMath.getIndex(section.getInt("slot.row"), section.getInt("slot.column"));
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

    public void insert() {
        this.insert(this.slot);
    }

    public void insert(byte slot) {
        if (this.menu != null) this.insert(this.menu, slot);
    }

    public void insert(IMenu aMenu, byte slot) {
        this.menu = aMenu;
        this.slot = slot;

        this.updateDescription();
        aMenu.getInventory().setItem(slot, this.getItemStack());
    }

    public void insert(IMenu aMenu) {
        this.insert(aMenu, this.slot);
    }

    public void insert(IMenu aMenu, byte row, byte column) {
        this.insert(aMenu, (byte) LunaMath.getIndex(row, column));
    }

    public Item replaceLore(UnaryOperator<String> operator) {
        List<String> newLore = new ArrayList<>(this.defaultLore);
        newLore.replaceAll(operator);
        this.setLore(newLore);
        return this;
    }

    public void updateDescription() {
        if (this.menu == null) return;

        List<String> lore = new ArrayList<>(this.defaultLore);
        lore.forEach(lr -> PlaceholderAPI.setPlaceholders(this.menu.getPlayer(), lr));
        this.setLore(lore);
    }

    public void remove(IMenu iMenu) {
        iMenu.getInventory().setItem(this.slot, null);
    }

    @Override
    public boolean equals(Object item) {
        if (this == item) return true;
        if (item == null || getClass() != item.getClass()) return false;
        if (!super.equals(item)) return false;
        Item thatItem = (Item) item;
        return Objects.equals(getMenu(), thatItem.getMenu());
    }

    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
