package org.novasparkle.lunaspring.API.util.utilities.rarities.loot;

import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Setter
public abstract class InventoryLoot<T, E> extends Loot<T, E> {
    private byte attempts = 15;
    public InventoryLoot(E object, Collection<T> collection, int maximumItems) {
        super(object, collection, maximumItems);
    }

    public InventoryLoot(E object, int maximumItems) {
        super(object, maximumItems);
    }

    public void insert(Inventory inventory) {
        List<T> list = new ArrayList<>(this.getList());

        byte attempts = 0;
        for (byte i = 0; i < this.getMaximumItems(); i++) {
            if (attempts >= this.attempts || list.isEmpty()) break;

            byte slot = (byte) ThreadLocalRandom.current().nextInt(inventory.getSize());
            ItemStack itemStack = inventory.getItem(slot);
            if (itemStack != null && itemStack.getType().isAir()) {
                attempts++;
                continue;
            }

            int index = ThreadLocalRandom.current().nextInt(list.size());
            T item = list.get(index);
            list.remove(index);

            this.insert(inventory, slot, item);
            attempts = 0;
        }
    }

    public abstract void insert(Inventory inventory, byte slot, T item);
}
