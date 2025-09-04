package org.novasparkle.lunaspring.API.menus.items;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.util.exceptions.SlotIsNotPositiveException;
import org.novasparkle.lunaspring.API.util.service.managers.NBTManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(chain = true, fluent = false)
@SuppressWarnings({"unused"})
public class Item extends NonMenuItem {
    public static final String MARKER_NBT = "lunaspring_marker_menu_item";

    @Setter protected List<String> defaultLore;
    @Setter protected String defaultName;

    protected ItemListMenu menu;
    @Setter protected byte slot = 0;

    @Builder
    public Item(Material material, String displayName, List<String> lore, int amount, @Range(from = 0, to = 53) byte slot) {
        super(material, displayName, lore, amount);
        this.slot = slot;
        this.defaultLore = new ArrayList<>(this.getLore());
        this.defaultName = this.getDisplayName();
        this.applyMenuNBT();
    }

    public Item(Material material, int amount) {
        super(material, amount);
        this.defaultLore = new ArrayList<>(this.getLore());
        this.defaultName = this.getDisplayName();
        this.applyMenuNBT();
    }

    public Item(NonMenuItem nonMenuItem, @Range(from = 0, to = 53) byte slot) {
        this(nonMenuItem.getMaterial(), nonMenuItem.getDisplayName(), nonMenuItem.getLore(), nonMenuItem.getAmount(), slot);
    }

    public Item() {
        super();
        this.defaultLore = new ArrayList<>(this.getLore());
        this.defaultName = this.getDisplayName();
        this.applyMenuNBT();
    }

    public Item(Material material) {
        this(material, 1);
    }

    public Item(Material material, @Range(from = 0, to = 53) byte slot) {
        this(material);
        this.slot = slot;
    }

    public Item(@NotNull ConfigurationSection section, @Range(from = 0, to = 53) int slot) {
        super(section);
        this.slot = (byte) slot;
        this.defaultLore = new ArrayList<>(this.getLore());
        this.defaultName = this.getDisplayName();
        this.applyMenuNBT();
    }

    public Item(@NotNull ConfigurationSection section, boolean rowCol) {
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

    public Item insert(@NotNull ItemListMenu itemListMenu, @Range(from = 0, to = 53) byte slot) {
        this.menu = itemListMenu;
        this.slot = slot;

        itemListMenu.getInventory().setItem(slot, this.getItemStack());
        return this;
    }

    public Item insert(@NotNull ItemListMenu itemListMenu) {
        this.insert(itemListMenu, this.slot);
        return this;
    }

    public Item insert(@NotNull ItemListMenu itemListMenu, @Range(from = 0, to = 6) byte row, @Range(from = 0, to = 9) byte column) {
        this.insert(itemListMenu, (byte) LunaMath.getIndex(row, column));
        return this;
    }

    @Override
    public NonMenuItem setAll(@NotNull ConfigurationSection itemSection) throws SlotIsNotPositiveException {
        super.setAll(itemSection);
        int row = itemSection.getInt("slot.row");
        int column = itemSection.getInt("slot.column");
        if (row != 0 && column != 0) {
            this.setSlot(((byte) LunaMath.getIndex(row, column)));
        } else {
            int slot = itemSection.getInt("slot");
            if (slot < 0) throw new SlotIsNotPositiveException();
            this.setSlot((byte) slot);
        }
        this.defaultLore = new ArrayList<>(itemSection.getStringList("lore"));
        this.defaultName = itemSection.getString("displayName");
        return this;
    }

    public NonMenuItem setAll(Material material, int amount, String displayName, List<String> lore, boolean enchanted, int slot) {
        super.setAll(material, amount, displayName, lore, enchanted);
        this.defaultLore = new ArrayList<>(lore);
        this.defaultName = displayName;
        this.slot = (byte) slot;
        return this;
    }

    public NonMenuItem setAll(Material material, int amount, String displayName, List<String> lore, boolean enchanted, int row, int column) {
        super.setAll(material, amount, displayName, lore, enchanted);
        this.defaultLore = new ArrayList<>(lore);
        this.defaultName = displayName;
        this.slot = (byte) LunaMath.getIndex(row, column);
        return this;
    }

    public Item applyMenuNBT() {
        if (!this.getClass().isAnnotationPresent(IgnoreMenuNBT.class)) {
            NBTManager.setBool(this.getItemStack(), MARKER_NBT, true);
        }

        return this;
    }

    public boolean equalsStacks(ItemStack itemStack) {
        ItemStack forCheckItem = this.getItemStack().clone();
        NBTManager.removeKey(forCheckItem, MARKER_NBT);
        return itemStack.equals(forCheckItem);
    }

    @Override
    public NonMenuItem update() {
        super.update();
        this.applyMenuNBT();
        return this;
    }

    public Item updateDescription(List<String> lore) {
        if (this.menu == null) return this;

        lore = new ArrayList<>(lore);
        lore.forEach(lr -> PlaceholderAPI.setPlaceholders(this.menu.getPlayer(), lr));
        this.setLore(lore);
        return this;
    }

    public Item updateDescription() {
        return this.updateDescription(this.defaultLore);
    }

    public Item remove(@NotNull ItemListMenu itemListMenu) {
        itemListMenu.getItemList().remove(this);
        itemListMenu.getInventory().setItem(this.slot, null);
        return this;
    }

    public Item onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        return this;
    }

    @Override
    public Item clone() throws CloneNotSupportedException {
        Item copy = (Item) super.clone();
        copy.defaultLore = new ArrayList<>(this.defaultLore);
        return copy;
    }

    public static boolean isMarkered(ItemStack itemStack) {
        return itemStack != null && !itemStack.getType().isAir() && NBTManager.hasTag(itemStack, MARKER_NBT);
    }
}