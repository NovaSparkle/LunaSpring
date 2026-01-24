package org.novasparkle.lunaspring.API.menus.updatable;

import org.bukkit.event.inventory.InventoryOpenEvent;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.menus.updatable.tasks.UpdatableTask;
import org.novasparkle.lunaspring.LunaSpring;

public interface UpdatableIMenu extends ItemListMenu {
    UpdatableTask getRunnable();

    @Override
    default void onOpen(InventoryOpenEvent e) {
        if (this.getRunnable() == null) return;
        this.getRunnable().runTaskAsynchronously(LunaSpring.getInstance());
    }
}
