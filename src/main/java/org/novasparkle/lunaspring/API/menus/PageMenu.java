package org.novasparkle.lunaspring.API.menus;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class PageMenu<T> extends AMenu {
    protected final List<List<T>> itemList = new ArrayList<>();
    @Setter
    @Getter
    private int page;
    @Getter
    private final List<Integer> itemsOrder;
    public PageMenu(Player player, List<Integer> itemsOrder) {
        super(player);
        this.itemsOrder = itemsOrder;
        this.page = 1;
        if (this.itemsOrder.isEmpty()) {
            throw new RuntimeException("Список слотов кнопок не может быть пустым!");
        }
    }

    public void partition(List<T> classifiedItems) {
        for (int i = 0; i < classifiedItems.size(); i += itemsOrder.size()) {
            int end = Math.min(i + itemsOrder.size(), classifiedItems.size());
            this.itemList.add(classifiedItems.subList(i, end));
        }
    }

    public abstract void reloadPage(int page);

    public class NextButton extends Item {
        public NextButton(ConfigurationSection section, boolean rowCol) {
            super(section, rowCol);
        }

        @Override
        public Item onClick(InventoryClickEvent event) {
            PageMenu.this.reloadPage(PageMenu.this.page + 1);
            return this;
        }
    }

    public class PreviousButton extends Item {

        public PreviousButton(ConfigurationSection section, boolean rowCol) {
            super(section, rowCol);
        }

        @Override
        public Item onClick(InventoryClickEvent event) {
            PageMenu.this.reloadPage(PageMenu.this.page - 1);
            return this;
        }
    }
}
