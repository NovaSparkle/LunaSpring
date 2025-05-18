package org.novasparkle.lunaspring.API.menus;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class MenuManager {
    @Getter private final HashMap<Inventory, IMenu> activeInventories = new HashMap<>();
    public void openInventory(Player player, IMenu menu) {
        register(menu.getInventory(), menu);
        player.openInventory(menu.getInventory());
    }

    public void register(Inventory inventory, IMenu menu) {
        activeInventories.put(inventory, menu);
    }

    public void unregister(Inventory inventory) {
        activeInventories.remove(inventory);
    }

    public void handleOpen(InventoryOpenEvent event) {
        IMenu menu = activeInventories.get(event.getInventory());
        if (menu != null) menu.onOpen(event);
    }

    public void handleClick(InventoryClickEvent event) {
        IMenu menu = activeInventories.get(event.getInventory());
        if (menu != null && !menu.isCancelled(event, event.getRawSlot())) menu.onClick(event);
    }

    public void handleDrag(InventoryDragEvent event) {
        IMenu menu = activeInventories.get(event.getInventory());
        if (menu != null) menu.onDrag(event);

    }

    public void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        IMenu menu = activeInventories.get(event.getInventory());
        if (menu != null) {
            menu.onClose(event);
            unregister(inventory);
        }
    }

    public Set<Player> getActiveViewers(Inventory inventory) {
        return activeInventories.entrySet().stream().filter(entry -> entry.getKey().equals(inventory)).map(e -> e.getValue().getPlayer()).collect(Collectors.toSet());
    }

    public Set<Player> getActiveViewers(IMenu iMenu) {
        return activeInventories.values().stream().filter(menu -> menu.equals(iMenu)).map(IMenu::getPlayer).collect(Collectors.toSet());
    }
}
