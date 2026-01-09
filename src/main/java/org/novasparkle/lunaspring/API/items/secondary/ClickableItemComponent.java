package org.novasparkle.lunaspring.API.items.secondary;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.items.Component;
import org.novasparkle.lunaspring.API.items.ItemComponent;

@Component
public interface ClickableItemComponent extends ItemComponent {
    boolean onClick(PlayerInteractEvent event, ItemStack itemStack);
}
