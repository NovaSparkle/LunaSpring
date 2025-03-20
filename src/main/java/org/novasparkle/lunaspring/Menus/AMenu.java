package org.novasparkle.lunaspring.Menus;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Events.CooldownPrevent;
import org.novasparkle.lunaspring.Menus.Items.Decoration;
import org.novasparkle.lunaspring.Menus.Items.Item;
import org.novasparkle.lunaspring.Util.LunaMath;
import org.novasparkle.lunaspring.Util.managers.ColorManager;
import org.novasparkle.lunaspring.Util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AMenu implements IMenu {
    @Getter private Inventory inventory;
    @Getter private String title;
    @Getter @Setter private Decoration decoration;
    @Getter private final Player player;
    private final CooldownPrevent<Integer> cooldownPrevent = new CooldownPrevent<>();
    private final List<Item> itemList = new ArrayList<>();

    @SuppressWarnings("deprecation")
    public AMenu(Player player, String title, byte size) {
        this.player = player;
        this.title = title;
        this.inventory = Bukkit.createInventory(this.player, LunaMath.max(size, 54), Utils.color(title));
    }

    @SuppressWarnings("deprecation")
    public AMenu(Player player, ConfigurationSection menuSection) {
        this.player = player;
        this.title = menuSection.getString("title");
        this.inventory = Bukkit.createInventory(this.player,
                LunaMath.max(menuSection.getInt("size"), 54), ColorManager.color(this.title));
        this.decoration = new Decoration(Objects.requireNonNull(menuSection.getConfigurationSection("decoration")));
        this.decoration.insert(this);
    }

    @SuppressWarnings("deprecation")
    public AMenu(Player player, String title, byte size, ConfigurationSection decorSection) {
        this.player = player;
        this.title = title;
        this.inventory = Bukkit.createInventory(this.player, LunaMath.max(size, 54), ColorManager.color(title));
        this.decoration = new Decoration(decorSection);
        this.decoration.insert(this);
    }

    public AMenu(Player player) {
        this.player = player;
    }

    @Override
    public boolean isCancelled(Cancellable event, int slot) {
        return this.cooldownPrevent.isCancelled(event, slot);
    }

    public void setClickCooldown(int millis) {
        this.cooldownPrevent.setCooldownMS(millis);
    }

    @SuppressWarnings("deprecation")
    public void initialize(ConfigurationSection section, boolean decorate) {
        this.title = section.getString("title");
        assert this.title != null;
        this.inventory = Bukkit.createInventory(this.player, section.getInt("size"), ColorManager.color(this.title));
        if (decorate) {
            this.decoration = new Decoration(Objects.requireNonNull(section.getConfigurationSection("decoration")));
            this.decoration.insert(this);
        }
    }

    @Override
    public String toString() {
        return "AMenu{" +
                "player=" + this.player +
                ", title=" + this.title +
                ", itemList=" + this.itemList +
                '}';
    }

    @SuppressWarnings("deprecation")
    public void initialize(String title, byte size, ConfigurationSection decorSection, boolean decorate) {
        this.inventory = Bukkit.createInventory(this.player, size, ColorManager.color(title));
        this.title = title;
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
    public Item findFirstItem(Class<?> clazz) {
        return this.itemList.stream().filter(i -> i.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public List<Item> findItems(Class<?> clazz) {
        return this.itemList.stream().filter(i -> i.getClass().equals(clazz)).collect(Collectors.toList());
    }
    public Item findFirstItem(String displayName) {
        return this.itemList.stream().filter(i -> i.getDisplayName().equals(displayName)).findFirst().orElse(null);
    }

    public List<Item> findItems(String displayName) {
        return this.itemList.stream().filter(i -> i.getDisplayName().equals(displayName)).collect(Collectors.toList());
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
