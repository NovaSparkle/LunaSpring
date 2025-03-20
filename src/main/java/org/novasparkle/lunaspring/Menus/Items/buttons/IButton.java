package org.novasparkle.lunaspring.Menus.Items.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IButton {
    void click(Player player);
    ItemStack getItemStack();
    byte getSlot();
}
