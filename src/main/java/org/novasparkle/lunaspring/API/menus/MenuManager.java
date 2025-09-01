package org.novasparkle.lunaspring.API.menus;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.novasparkle.lunaspring.API.util.service.realized.Cache;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Stream;

@UtilityClass
public class MenuManager {
    @Getter private final Map<Inventory, List<IMenu>> activeInventories = new HashMap<>();

    public void openInventory(IMenu menu) {
        register(menu.getInventory(), menu);
        menu.getPlayer().openInventory(menu.getInventory());
    }

    public void register(Inventory inventory, IMenu menu) {
        List<IMenu> menus = activeInventories.getOrDefault(inventory, new ArrayList<>());
        menus.add(menu);
        register(inventory, menus);
    }

    public void register(Inventory inventory, List<IMenu> menus) {
        activeInventories.putIfAbsent(inventory, menus);
    }

    public void unregister(IMenu iMenu) {
        Inventory inventory = iMenu.getInventory();
        List<IMenu> menus = activeInventories.get(inventory);
        if (menus != null) {
            activeInventories.remove(inventory);
            if (menus.size() > 1) {
                menus.remove(iMenu);
                activeInventories.put(inventory, menus);
            }
        }
    }

    public void handleOpen(InventoryOpenEvent event) {
        List<IMenu> menus = activeInventories.get(event.getInventory());
        if (menus != null && !menus.isEmpty()) {
            if (menus.size() > 1) {
                menus.stream().filter(m -> m.getPlayer().getUniqueId().equals(event.getPlayer().getUniqueId())).findFirst().ifPresent(clickedMenu -> clickedMenu.onOpen(event));
            } else {
                menus.get(0).onOpen(event);
            }
        }
    }

    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        List<IMenu> menus = activeInventories.get(inventory);
        if (menus != null && !menus.isEmpty()) {
            System.out.println(System.currentTimeMillis());
            if (menus.size() > 1) {
                menus.stream().filter(m -> m.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().ifPresent(clickedMenu -> clickedMenu.onClick(event));
            } else {
                menus.get(0).onClick(event);
            }
        }
    }

    public void handleDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        List<IMenu> menus = activeInventories.get(inventory);
        if (menus != null && !menus.isEmpty()) {
            if (menus.size() > 1) {
                menus.stream().filter(m -> m.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().ifPresent(clickedMenu -> clickedMenu.onDrag(event));
            } else {
                menus.get(0).onDrag(event);
            }
        }
    }

    public void handleClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        List<IMenu> menus = activeInventories.get(inventory);
        if (menus != null && !menus.isEmpty()) {
            IMenu iMenu = menus.stream().filter(m -> m.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
            if (iMenu != null) {
                iMenu.onClose(event);
                Bukkit.getScheduler().runTaskLater(LunaSpring.getInstance(), () -> {
                    unregister(iMenu);
                }, 2L);
            }
        }
    }

    public Stream<Player> getActiveViewers(Class<?> menuClass, boolean hardCheck) {
        Predicate<IMenu> predicate = hardCheck ? iMenu -> menuClass.equals(iMenu.getClass()) : iMenu -> menuClass.isAssignableFrom(iMenu.getClass());
        return activeInventories.values().stream().flatMap(List::stream).filter(predicate).map(IMenu::getPlayer);
    }

    public IMenu getActiveMenu(Player player) {
        return activeInventories.values().stream().flatMap(List::stream).filter(m -> m.getPlayer().equals(player)).findFirst().orElse(null);
    }

    public <T> Stream<T> getActiveMenus(Class<T> menuClass, boolean hardCheck) {
        Predicate<IMenu> predicate = hardCheck ? iMenu -> menuClass.equals(iMenu.getClass()) : iMenu -> menuClass.isAssignableFrom(iMenu.getClass());
        return activeInventories.values()
                .stream()
                .flatMap(List::stream)
                .filter(predicate)
                .map(menuClass::cast);
    }
}
