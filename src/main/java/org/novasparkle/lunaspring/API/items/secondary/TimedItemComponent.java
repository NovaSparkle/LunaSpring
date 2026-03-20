package org.novasparkle.lunaspring.API.items.secondary;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.items.Component;
import org.novasparkle.lunaspring.API.items.SlotFilteringComponent;

import java.util.List;
import java.util.stream.Stream;

@Component
public interface TimedItemComponent extends SlotFilteringComponent {
    default boolean isAsync() {
        return true;
    }
    void tick(Player handler, List<ItemStack> componentItems);
}
