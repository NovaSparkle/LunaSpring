package org.novasparkle.lunaspring.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.novasparkle.lunaspring.Menus.MenuManager;

public class MenuHandler implements Listener {
    @EventHandler
    private void onOpen(InventoryOpenEvent e) {
        MenuManager.handleOpen(e);
    }
    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        MenuManager.handleClose(e);
    }
    @EventHandler
    private void onClick(InventoryClickEvent e) {
        MenuManager.handleClick(e);
    }
}