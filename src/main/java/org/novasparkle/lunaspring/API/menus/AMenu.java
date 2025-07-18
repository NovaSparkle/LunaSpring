package org.novasparkle.lunaspring.API.menus;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.novasparkle.lunaspring.API.events.CooldownPrevent;
import org.novasparkle.lunaspring.API.menus.items.Decoration;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Getter
@SuppressWarnings({"deprecation", "unused"})
public abstract class AMenu implements IMenu {
    private Inventory inventory;
    private String title;
    @Setter private Decoration decoration;
    private CooldownPrevent<Integer> cooldownPrevent = new CooldownPrevent<>();
    private Player player;

    private final List<Item> itemList = new ArrayList<>();

    public AMenu(@NonNull Player player, String title, @Range(from = 9L, to=54) byte size) {
        this.player = player;
        this.title = title;
        this.inventory = Bukkit.createInventory(this.player, size, Utils.color(title));
    }

    public AMenu(@NonNull Player player, ConfigurationSection menuSection) {
        this.player = player;
        this.title = menuSection.getString("title");
        this.inventory = Bukkit.createInventory(this.player,
                Math.min(menuSection.getInt("size"), 54), ColorManager.color(this.title));
        this.decoration = new Decoration(Objects.requireNonNull(menuSection.getConfigurationSection("decoration")), this.inventory);
        this.decoration.insert();
    }

    public AMenu(@NonNull Player player, String title, @Range(from = 9L, to=54) byte size, ConfigurationSection decorSection) {
        this.player = player;
        this.title = title;
        this.inventory = Bukkit.createInventory(this.player, size, ColorManager.color(title));
        this.decoration = new Decoration(decorSection, this.inventory);
        this.decoration.insert();
    }

    public AMenu(@NonNull Player player) {
        this.player = player;
    }

    @Override
    public boolean isCancelled(@Nullable Cancellable event, int slot) {
        return this.cooldownPrevent.isCancelled(event, slot);
    }

    public void setClickCooldown(int millis) {
        this.cooldownPrevent.setCooldownMS(millis);
    }

    public void initialize(ConfigurationSection section, boolean decorate) {
        this.initialize(section.getString("title"), (byte) section.getInt("size"), section.getConfigurationSection("decoration"), decorate);
    }

    public void initialize(String title, byte size, ConfigurationSection decorSection, boolean decorate) {
        this.inventory = Bukkit.createInventory(this.player, size, ColorManager.color(title));
        this.title = title;
        if (decorate) {
            this.decoration = new Decoration(decorSection, this.inventory);
            this.decoration.insert();
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
    public void clear() {
        this.itemList.clear();
    }

    public List<Item> findItems(ItemStack itemStack) {
        return this.itemList.stream().filter(i -> i.getItemStack().equals(itemStack)).collect(Collectors.toList());
    }

    public Item findFirstItem(ItemStack itemStack) {
        return Utils.find(this.itemList, i -> i.getItemStack().equals(itemStack)).orElse(null);
    }

    public List<Item> findItems(Material material) {
        return this.itemList.stream().filter(i -> i.getItemStack().getType().equals(material)).collect(Collectors.toList());
    }
    public Item findFirstItem(Class<?> clazz) {
        return Utils.find(this.itemList, i -> i.getClass().equals(clazz)).orElse(null);
    }

    public List<Item> findItems(Class<?> clazz) {
        return this.itemList.stream().filter(i -> i.getClass().equals(clazz)).collect(Collectors.toList());
    }
    public Item findFirstItem(String displayName) {
        return Utils.find(this.itemList, i -> i.getDisplayName().equals(displayName)).orElse(null);
    }

    public List<Item> findItems(String displayName) {
        return this.itemList.stream().filter(i -> i.getDisplayName().equals(displayName)).collect(Collectors.toList());
    }

    public Item findFirstItem(Material material) {
        return Utils.find(this.itemList, i -> i.getMaterial().equals(material)).orElse(null);
    }

    public boolean itemClick(@NonNull Material material, InventoryClickEvent event) {
        Item item = this.findFirstItem(material);
        if (item != null) {
            item.onClick(event);
            return true;
        }
        return false;
    }
    public boolean itemClick(@NonNull String displayName, InventoryClickEvent event) {
        Item item = this.findFirstItem(displayName);
        if (item != null) {
            item.onClick(event);
            return true;
        }
        return false;
    }
    public boolean itemClick(@NonNull Class<?> clazz, InventoryClickEvent event) {
        Item item = this.findFirstItem(clazz);
        if (item != null) {
            item.onClick(event);
            return true;
        }
        return false;
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

    public AMenu clone(Player player) throws CloneNotSupportedException {
        AMenu copyMenu = (AMenu) super.clone();
        copyMenu.player = player;
        copyMenu.decoration = this.decoration.clone();
        copyMenu.inventory = this.getInventory();
        copyMenu.cooldownPrevent = this.cooldownPrevent.clone();
        System.out.println(copyMenu.inventory == this.getInventory());
        return copyMenu;
    }
}
