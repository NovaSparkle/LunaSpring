package org.novasparkle.lunaspring.API.menus.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.MenuManager;

import java.util.List;
import java.util.function.Function;

@Setter
@Getter @SuppressWarnings("unused")
public class SwitchItem extends Item {
    private Function<Player, IMenu> menuFunction;
    public SwitchItem(Material material,
                      String displayName,
                      List<String> lore,
                      int amount,
                      byte slot,
                      Function<Player, IMenu> menuFunction) {
        super(material, displayName, lore, amount, slot);
        this.menuFunction = menuFunction;
    }

    public SwitchItem(Material material, int amount, Function<Player, IMenu> menuFunction) {
        super(material, amount);
        this.menuFunction = menuFunction;
    }

    public SwitchItem(Material material, Function<Player, IMenu> menuFunction) {
        super(material);
        this.menuFunction = menuFunction;
    }

    public SwitchItem(Material material, byte slot, Function<Player, IMenu> menuFunction) {
        super(material, slot);
        this.menuFunction = menuFunction;
    }

    public SwitchItem(ConfigurationSection section, int slot, Function<Player, IMenu> menuFunction) {
        super(section, slot);
        this.menuFunction = menuFunction;
    }

    public SwitchItem(ConfigurationSection section, boolean rowCol, Function<Player, IMenu> menuFunction) {
        super(section, rowCol);
        this.menuFunction = menuFunction;
    }
    @Override
    public SwitchItem onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuManager.openInventory(this.menuFunction.apply(player));
        return this;
    }
}
