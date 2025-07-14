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
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
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
        if (menu != null && getActiveViewers(menu.getClass(), true).size() <= 1) {
            menu.onClose(event);
            unregister(inventory);
        }
    }

    public List<Player> getActiveViewers(Class<?> menuClass, boolean hardCheck) {
        Predicate<Map.Entry<Inventory, IMenu>> predicate = hardCheck ? entry -> entry.getKey().getClass().equals(menuClass) :
                entry -> menuClass.isAssignableFrom(entry.getKey().getClass());
        return activeInventories.entrySet().stream().filter(predicate).map(e -> e.getValue().getPlayer()).collect(Collectors.toList());
    }

    public IMenu getActiveMenu(Player player) {
        return activeInventories.values().stream().filter(m -> m.getPlayer().equals(player)).findFirst().orElse(null);
    }
}
