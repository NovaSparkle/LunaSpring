package org.novasparkle.lunaspring.API.menus;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.items.Item;

import java.util.Collection;
import java.util.List;

public interface ItemListMenu extends IMenu {
    List<Item> getItemList();
    Collection<Item> findItems(ItemStack itemStack);
    Collection<Item> findItems(Material material);
    Collection<Item> findItems(Class<?> clazz);
    Collection<Item> findItems(String displayName);

    Item findFirstItem(ItemStack itemStack);
    Item findFirstItem(Class<?> clazz);
    Item findFirstItem(String displayName);
    Item findFirstItem(Material material);

    boolean itemClick(@NotNull Material material, InventoryClickEvent event);
    boolean itemClick(@NotNull String displayName, InventoryClickEvent event);
    boolean itemClick(@NotNull Class<?> clazz, InventoryClickEvent event);
    boolean itemClick(@NotNull ItemStack itemStack, InventoryClickEvent event);

    void addItems(Item... items);
    Collection<Item> addItems(Collection<Item> items);

    Collection<Item> insertAllItems();
    void clear();
}
