package org.novasparkle.lunaspring.API.util.utilities.lists;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

@UtilityClass
public class LunaLists {
    @SafeVarargs
    public <E> GenericList<E> newList(E... elements) {
        return new GenericList<>(elements);
    }

    public <E> GenericList<E> newList(Collection<E> collection) {
        return new GenericList<>(collection);
    }

    public ItemStackList newStacks(ItemStack... itemStacks) {
        return new ItemStackList(itemStacks);
    }

    public ItemStackList newStacks(Collection<ItemStack> itemStacks) {
        return new ItemStackList(itemStacks);
    }
}
