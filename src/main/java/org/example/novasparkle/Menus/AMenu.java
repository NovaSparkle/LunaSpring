package org.example.novasparkle.Menus;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.example.novasparkle.Items.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AMenu implements IMenu {

    @Getter
    private final Inventory inventory;
    @Getter
    private final Player player;
    private final Decoration decoration;
    protected final List<Item> itemList = new ArrayList<>();


    @SuppressWarnings("deprecation")
    public AMenu(Player player, ConfigurationSection section) {
        this.player = player;

        String title = section.getString("menuTitle");
        assert title != null;
        this.inventory = Bukkit.createInventory(this.player, section.getInt("menuSize"), title);

        ConfigurationSection decorationSection = section.getConfigurationSection("decoration");
        assert decorationSection != null;
        this.decoration = new Decoration(decorationSection);

        this.decoration.insert(this);
    }
    @SuppressWarnings("deprecation")
    public AMenu(Player player, String title, byte size, ConfigurationSection decorationSection) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this.player, size, title);
        this.decoration = new Decoration(decorationSection);
        this.decoration.insert(this);
    }
    public List<Item> findItems(ItemStack itemStack) {
        return this.itemList.stream().filter(i -> i.getItemStack().equals(itemStack)).collect(Collectors.toList());
    }
    public Item findFirstItem(ItemStack itemStack) {
        return this.itemList.stream().filter(i -> i.getItemStack().equals(itemStack)).findFirst().orElse(null);
    }
    public List<Item> findItems(Material material) {
        return this.itemList.stream().filter(i -> i.getItemStack().getType().equals(material)).collect(Collectors.toList());
    }
    public Item findFirstItem(Material material) {
        return this.itemList.stream().filter(i -> i.getMaterial().equals(material)).findFirst().orElse(null);
    }
    public void insertAllItems() {
        this.itemList.forEach(i -> i.insert(this.getInventory()));
    }
    public void fillItemsList(Item... items) {
        this.itemList.addAll(Arrays.asList(items));
    }
    public void fillItemsList(List<Item> items) {
        this.itemList.addAll(items);
    }
}
