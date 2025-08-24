package org.novasparkle.lunaspring.API.util.utilities.rarities.loot;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;

import java.util.Collection;
import java.util.List;

@Getter
public abstract class Loot<T, E> {
    private final List<T> list = Lists.newArrayList();
    private final int maximumItems;
    public Loot(E object, Collection<T> collection, int maximumItems) {
        this.list.addAll(collection);
        this.maximumItems = maximumItems;
    }

    public Loot(E object, int maximumItems) {
        this.maximumItems = maximumItems;
    }

    public void add(T t) {
        this.list.add(t);
    }

    public void remove(T t) {
        this.list.remove(t);
    }

    protected abstract void generate(E object);

    public static @NotNull ItemStack getStackFromSection(ConfigurationSection parentSection, String key) {
        ItemStack itemStack = parentSection.getItemStack(key);
        if (itemStack == null) {
            ConfigurationSection itemSection = parentSection.getConfigurationSection(key);
            assert itemSection != null;

            return new NonMenuItem(itemSection).getItemStack();
        } else itemStack = itemStack.clone();
        return itemStack;
    }
}
