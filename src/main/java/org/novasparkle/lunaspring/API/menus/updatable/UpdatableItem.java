package org.novasparkle.lunaspring.API.menus.updatable;

import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;

public interface UpdatableItem {
    ItemStack getItemStack();
    Item insert(ItemListMenu menu);
    void tick(UpdatableIMenu updatableMenu);
}
