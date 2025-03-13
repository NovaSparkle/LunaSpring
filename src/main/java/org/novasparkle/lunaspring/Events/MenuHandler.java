package org.novasparkle.lunaspring.Events;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Menus.MenuManager;

public class MenuHandler implements Listener {
    @Getter private static CooldownPrevent<Integer> cooldown;
    public MenuHandler() {
        cooldown = new CooldownPrevent<>();
        FileConfiguration config = LunaSpring.getINSTANCE().getConfig();
        if (config.getBoolean("preventDoubleClick.enabled")) {
            cooldown.setCooldownMS(config.getInt("preventDoubleClick.cooldown"));
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
        if (!cooldown.cancelEvent(e, e.getRawSlot())) MenuManager.handleClick(e);
    }
}