package org.novasparkle.lunaspring.API.items;

import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;
import org.novasparkle.lunaspring.API.util.service.managers.NBTManager;

@Component
public interface ItemComponent {
    String getId();
    NonMenuItem createItem();
    default boolean itemIsComponent(ItemStack itemStack) {
        return itemStack != null && !itemStack.getType().isAir() && NBTManager.hasTag(itemStack, getId());
    }
}