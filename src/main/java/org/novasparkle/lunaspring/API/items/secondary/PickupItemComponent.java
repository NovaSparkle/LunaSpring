package org.novasparkle.lunaspring.API.items.secondary;

import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.items.Component;
import org.novasparkle.lunaspring.API.items.SlotFilteringComponent;

@Component
public interface PickupItemComponent extends SlotFilteringComponent {
    void onPickup(PlayerAttemptPickupItemEvent e, ItemStack itemStack);
}
