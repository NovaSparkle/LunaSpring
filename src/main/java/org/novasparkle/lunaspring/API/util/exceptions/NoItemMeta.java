package org.novasparkle.lunaspring.API.util.exceptions;

import org.bukkit.inventory.ItemStack;

public class NoItemMeta extends LunaException {
    public NoItemMeta(ItemStack itemStack) {
        super(String.format("У ItemStack типа %s отсутствует ItemMeta!", itemStack.getType()));
    }
}
