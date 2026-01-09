package org.novasparkle.lunaspring.API.items.secondary;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.items.Component;
import org.novasparkle.lunaspring.API.items.ItemComponent;

@Component
public interface InventoryClickItemComponent extends ItemComponent {
    void onClick(InventoryClickEvent event, ItemStack itemStack, boolean inCursor);
}