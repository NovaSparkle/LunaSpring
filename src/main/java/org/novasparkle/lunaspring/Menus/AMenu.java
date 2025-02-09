package org.novasparkle.lunaspring.Menus;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Menus.Items.Decoration;
import org.novasparkle.lunaspring.Menus.Items.Item;
import org.novasparkle.lunaspring.Util.managers.ColorManager;
import org.novasparkle.lunaspring.Util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AMenu implements IMenu {
    @Getter private Inventory inventory;
    @Getter private final Player player;
    @Getter private Decoration decoration;
    private final List<Item> itemList = new ArrayList<>();

    @SuppressWarnings("deprecation")
    public AMenu(Player player, String title, byte size) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this.player, size, Utils.color(title));
    }

    @SuppressWarnings("deprecation")
    public AMenu(Player player, ConfigurationSection menuSection) {
        this.player = player;
        String title = menuSection.getString("title");
        this.inventory = Bukkit.createInventory(this.player, menuSection.getInt("size"), ColorManager.color(title));
        this.decoration = new Decoration(Objects.requireNonNull(menuSection.getConfigurationSection("decoration")));
        this.decoration.insert(this);
    }

    @SuppressWarnings("deprecation")
    public AMenu(Player player, String title, byte size, ConfigurationSection decorSection) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this.player, size, ColorManager.color(title));
        this.decoration = new Decoration(decorSection);
        this.decoration.insert(this);
    }

    public AMenu(Player player) {
        this.player = player;
    }

    @SuppressWarnings("deprecation")
    public void initialize(ConfigurationSection section, boolean decorate) {
        String title = section.getString("title");
        assert title != null;
        this.inventory = Bukkit.createInventory(this.player, section.getInt("size"), ColorManager.color(title));
        if (decorate) {
            this.decoration = new Decoration(Objects.requireNonNull(section.getConfigurationSection("decoration")));
            this.decoration.insert(this);
        }
    }

    @Override
    public String toString() {
        return "AMenu{" +
                "player=" + player +
                ", itemList=" + itemList +
                '}';
    }

    @SuppressWarnings("deprecation")
    public void initialize(String title, byte size, ConfigurationSection decorSection, boolean decorate) {
        this.inventory = Bukkit.createInventory(this.player, size, ColorManager.color(title));
        if (decorate) {
            this.decoration = new Decoration(decorSection);
            this.decoration.insert(this);
        }
    }
    public void clear() {
        this.itemList.clear();
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
        this.itemList.forEach(i -> i.insert(this));
    }

    public void addItems(Item... items) {
        this.itemList.addAll(Arrays.asList(items));
    }

    public void addItems(List<Item> items) {
        this.itemList.addAll(items);
    }
    public void drawItems() {
        this.itemList.forEach(System.out::println);
    }
}
