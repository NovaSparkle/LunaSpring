package org.novasparkle.lunaspring.API.menus.items;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Range;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

@Getter
@Accessors(chain = true, fluent = false)
@SuppressWarnings({"unused"})
public class Item extends NonMenuItem {
    private final List<String> defaultLore;
    private final String defaultName;

    private ItemListMenu menu;
    @Setter private byte slot = 0;

    public Item(Material material, String displayName, List<String> lore, int amount, @Range(from = 0, to = 53) byte slot) {
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

    public Item(NonMenuItem nonMenuItem, @Range(from = 0, to = 53) byte slot) {
        this(nonMenuItem.getMaterial(), nonMenuItem.getDisplayName(), nonMenuItem.getLore(), nonMenuItem.getAmount(), slot);
    }

    public Item(Material material) {
        this(material, 1);
    }

    public Item(Material material, @Range(from = 0, to = 53) byte slot) {
        this(material);
        this.slot = slot;
    }

    public Item(@NonNull ConfigurationSection section, @Range(from = 0, to = 53) int slot) {
        super(section);
        this.slot = (byte) slot;
        this.defaultLore = new ArrayList<>(this.getLore());
        this.defaultName = this.getDisplayName();
    }

    public Item(@NonNull ConfigurationSection section, boolean rowCol) {
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

    public Item insert() {
        this.insert(this.slot);
        return this;
    }

    public Item insert(@Range(from = 0, to = 53) byte slot) {
        if (this.menu != null) this.insert(this.menu, slot);
        return this;
    }

    public Item insert(@NonNull ItemListMenu itemListMenu, @Range(from = 0, to = 53) byte slot) {
        this.menu = itemListMenu;
        this.slot = slot;

        itemListMenu.getInventory().setItem(slot, this.getItemStack());
        return this;
    }

    public Item insert(@NonNull ItemListMenu itemListMenu) {
        this.insert(itemListMenu, this.slot);
        return this;
    }

    public Item insert(@NonNull ItemListMenu itemListMenu, @Range(from = 0, to = 6) byte row, @Range(from = 0, to = 9) byte column) {
        this.insert(itemListMenu, (byte) LunaMath.getIndex(row, column));
        return this;
    }

    public Item replaceLore(UnaryOperator<String> operator) {
        this.getLore().replaceAll(operator);
        this.update();
        return this;
    }

    public Item updateDescription() {
        if (this.menu == null) return this;

        List<String> lore = new ArrayList<>(this.defaultLore);
        lore.forEach(lr -> PlaceholderAPI.setPlaceholders(this.menu.getPlayer(), lr));
        this.setLore(lore);
        return this;
    }

    public Item remove(@NonNull ItemListMenu itemListMenu) {
        itemListMenu.getItemList().remove(this);
        itemListMenu.getInventory().setItem(this.slot, null);
        return this;
    }

    @Override
    public boolean equals(Object item) {
        if (this == item) return true;
        if (item == null || getClass() != item.getClass()) return false;
        if (!super.equals(item)) return false;
        Item thatItem = (Item) item;
        return Objects.equals(getMenu(), thatItem.getMenu());
    }

    public Item onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        return this;
    }
}
