package org.novasparkle.lunaspring.API.menus;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuManager {
    @Getter
    private static final HashMap<Inventory, IMenu> activeInventories = new HashMap<>();
    public static void openInventory(Player player, IMenu menu) {
        register(menu.getInventory(), menu);
        player.openInventory(menu.getInventory());
    }

    private static void register(Inventory inventory, IMenu menu) {
        activeInventories.put(inventory, menu);
    }

    private static void unregister(Inventory inventory) {
        activeInventories.remove(inventory);
    }

    public static void handleOpen(InventoryOpenEvent event) {
        IMenu menu = activeInventories.get(event.getInventory());
        if (menu != null) {
            menu.onOpen(event);
        }
    }

    public static void handleClick(InventoryClickEvent event) {
        IMenu menu = activeInventories.get(event.getInventory());
        if (menu != null) {
            if (!menu.isCancelled(event, event.getRawSlot()))
                menu.onClick(event);
        }
    }

    public static void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        IMenu menu = activeInventories.get(event.getInventory());
        if (menu != null) {
            menu.onClose(event);
            unregister(inventory);
        }
    }
    public static Set<Player> getActiveViewers(Inventory inventory) {
        return activeInventories.entrySet().stream().filter(entry -> entry.getKey().equals(inventory)).map(e -> e.getValue().getPlayer()).collect(Collectors.toSet());
    }
    public static Set<Player> getActiveViewers(IMenu iMenu) {
        return activeInventories.values().stream().filter(menu -> menu.equals(iMenu)).map(IMenu::getPlayer).collect(Collectors.toSet());
    }
}
