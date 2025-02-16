package org.novasparkle.lunaspring.Events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Menus.MenuManager;

import java.util.HashMap;
import java.util.Map;

public class MenuHandler implements Listener {
    private final Map<Integer, Long> cooldowns;
    private int cooldown;
    public MenuHandler() {
        this.cooldowns = new HashMap<>();
        FileConfiguration config = LunaSpring.getINSTANCE().getConfig();
        if (config.getBoolean("preventDoubleClick.enabled")) {
            this.cooldown = config.getInt("preventDoubleClick.cooldown");
        }
    }

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
        if (cooldown == 0) {
            MenuManager.handleClick(e);
            return;
        }
        if (!this.cooldowns.containsKey(e.getRawSlot()) || this.cooldowns.get(e.getRawSlot()) < System.currentTimeMillis()) {

            this.cooldowns.put(e.getRawSlot(), System.currentTimeMillis() + this.cooldown);
            MenuManager.handleClick(e);
        } else {
            e.setCancelled(true);
        }
    }
}