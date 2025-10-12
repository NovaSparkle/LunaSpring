package org.novasparkle.lunaspring.API.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.MenuManager;
import org.novasparkle.lunaspring.API.menus.MoveIgnored;

public class MenuHandler implements Listener {
    private final Class<MoveIgnored> CACHED_ANNOTATION = MoveIgnored.class;

    @EventHandler
    private void onOpen(InventoryOpenEvent e) {
        MenuManager.handleOpen(e);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        MenuManager.handleClose(e);
    }

    @EventHandler
    private void onDrag(InventoryDragEvent e) {
        MenuManager.handleDrag(e);
    }

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        MenuManager.handleClick(e);
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.isOp() || player.hasPermission("lunaspring.moveignore")) return;

        if (e.hasChangedOrientation() && player.isGliding()) {
            this.closeIMenu(player);
            return;
        }

        if (!e.hasExplicitlyChangedBlock() || (player.getFallDistance() > 0 && !player.isOnGround()) || player.isFlying()) return;

        if (player.getWorld().getNearbyPlayers(player.getLocation(), 1.6).isEmpty())
            this.closeIMenu(player);
    }

    private void closeIMenu(Player player) {
        IMenu menu = MenuManager.getActiveMenu(player);
        if (menu != null && !menu.getClass().isAnnotationPresent(CACHED_ANNOTATION)) {
            player.closeInventory();
        }
    }
}