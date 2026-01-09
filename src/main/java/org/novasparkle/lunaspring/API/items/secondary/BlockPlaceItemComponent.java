package org.novasparkle.lunaspring.API.items.secondary;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.items.Component;
import org.novasparkle.lunaspring.API.items.ItemComponent;

@Component
public interface BlockPlaceItemComponent extends ItemComponent {
    boolean onPlace(BlockPlaceEvent e, ItemStack itemStack);
}
