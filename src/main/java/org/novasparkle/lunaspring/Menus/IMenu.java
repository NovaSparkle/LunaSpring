package org.novasparkle.lunaspring.Menus;

import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public interface IMenu {
    Inventory getInventory();
    void onOpen(InventoryOpenEvent event);
    void onClick(InventoryClickEvent event);
    void onClose(InventoryCloseEvent event);
    boolean isCancelled(Cancellable event, int slot);
}
