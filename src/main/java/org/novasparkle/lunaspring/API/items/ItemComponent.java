package org.novasparkle.lunaspring.API.items;

import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;

@Component
public interface ItemComponent {
    String getId();
    boolean itemIsComponent(ItemStack itemStack);
    NonMenuItem createItem();
}