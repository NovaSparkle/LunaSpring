package org.novasparkle.lunaspring.API.items.secondary;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.items.Component;
import org.novasparkle.lunaspring.API.items.SlotFilteringComponent;

import java.util.stream.Stream;

@Component
public interface TimedItemComponent extends SlotFilteringComponent {
    void tick(Player handler, Stream<ItemStack> componentItems);
}
