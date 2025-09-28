package org.novasparkle.lunaspring.API.items.secondary;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.items.Component;
import org.novasparkle.lunaspring.API.items.SlotFilteringComponent;

import java.util.stream.Stream;

@Component
public interface QuitItemComponent extends SlotFilteringComponent {
    void onQuit(Player handler, Stream<ItemStack> componentItems);
}