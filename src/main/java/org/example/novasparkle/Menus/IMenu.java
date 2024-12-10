package org.example.novasparkle.Menus;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public interface IMenu {
    Inventory getInventory();
    void onOpen(InventoryOpenEvent event);
    void onClick(InventoryClickEvent event);
    void onClose(InventoryCloseEvent event);
}
