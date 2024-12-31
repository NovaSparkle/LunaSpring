package org.example.novasparkle.Menus;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.example.novasparkle.Items.Item;
import org.example.novasparkle.Util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AMenu implements IMenu {

    @Getter
    private Inventory inventory;
    @Getter
    private final Player player;
    private Decoration decoration;
    protected final List<Item> itemList = new ArrayList<>();

    @SuppressWarnings("deprecation")
    public AMenu(Player player, String title, byte size, ConfigurationSection menuSection) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this.player, size, Utils.color(title));
        this.decoration = new Decoration(menuSection);
        this.decoration.insert(this);
    }
    public AMenu(Player player) {
        this.player = player;
    }

    @SuppressWarnings("deprecation")
    public void initialize(ConfigurationSection section) {
        String title = section.getString("title");
        this.decoration = new Decoration(section);
        assert title != null;
        this.inventory = Bukkit.createInventory(this.player, section.getInt("size"), title);
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

    public void addItems(Item... items) {
        this.itemList.addAll(Arrays.asList(items));
    }
    public void addItems(List<Item> items) {
        this.itemList.addAll(items);
    }
}
