package org.novasparkle.lunaspring.API.Menus;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public interface IMenu {
    Inventory getInventory();
    Player getPlayer();
    void onOpen(InventoryOpenEvent event);
    void onClick(InventoryClickEvent event);
    void onClose(InventoryCloseEvent event);
    boolean isCancelled(Cancellable event, int slot);
}
