package org.novasparkle.lunaspring.API.menus.items;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.MenuManager;

import java.util.List;

@Getter @SuppressWarnings("unused")
public class SwitchItem extends Item {
    private final IMenu toMenu;
    public SwitchItem(Material material,
                      String displayName,
                      List<String> lore,
                      int amount,
                      byte slot,
                      IMenu toMenu) {
        super(material, displayName, lore, amount, slot);
        this.toMenu = toMenu;
    }

    public SwitchItem(Material material, int amount, IMenu toMenu) {
        super(material, amount);
        this.toMenu = toMenu;
    }

    public SwitchItem(Material material, IMenu toMenu) {
        super(material);
        this.toMenu = toMenu;
    }

    public SwitchItem(Material material, byte slot, IMenu toMenu) {
        super(material, slot);
        this.toMenu = toMenu;
    }

    public SwitchItem(ConfigurationSection section, int slot, IMenu toMenu) {
        super(section, slot);
        this.toMenu = toMenu;
    }

    public SwitchItem(ConfigurationSection section, boolean rowCol, IMenu toMenu) {
        super(section, rowCol);
        this.toMenu = toMenu;
    }
    @Override
    public SwitchItem onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuManager.openInventory(player, this.toMenu);
        return this;
    }
}
