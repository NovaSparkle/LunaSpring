package org.novasparkle.lunaspring.API.menus.items;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;

import java.util.List;

public abstract class StoreItem extends Item {
    @Accessors(fluent = true, chain = true)
    private ItemStack storedItem;

    public StoreItem(Material material, String displayName, List<String> lore, int amount, @Range(from = 0, to = 53) byte slot) {
        super(material, displayName, lore, amount, slot);
    }

    public StoreItem(NonMenuItem nonMenuItem, @Range(from = 0, to = 53) byte slot) {
        super(nonMenuItem, slot);
    }

    public StoreItem(@NotNull ConfigurationSection section, boolean rowCol) {
        super(section, rowCol);
    }

    public StoreItem(@NotNull ConfigurationSection section, @Range(from = 0, to = 53) int slot) {
        super(section, slot);
    }

    @Override
    public Item insert() {
        ItemListMenu menu = this.getMenu();
        if (menu == null) return this;

        if (this.storedItem == null) {
            menu.getInventory().setItem(this.slot, this.storedItem);
        }
        else {
            super.insert();
        }

        return this;
    }

    @Override @SuppressWarnings("deprecation")
    public Item onClick(InventoryClickEvent e) {
        super.onClick(e);
        ItemStack cursor = e.getCursor();

        String name = cursor == null ? null : cursor.getItemMeta().getDisplayName();
        if (name != null) {
            if (name.isEmpty()) name = cursor.getType().name();
            else name = ColorManager.color(name);
        }

        Player player = (Player) e.getWhoClicked();
        if (this.storedItem == null) {
            if (cursor == null) {
                this.sendMessage(player, MessageID.NEED_PUT_ITEMS, "player-%-" + player.getName());
                return this;
            }

            if (!this.canPutItem(cursor)) {
                this.sendMessage(player, MessageID.DISABLE_PUTTING, "player-%-" + player.getName(), "item-%-" + name);
                return this;
            }

            this.setStoredItems(cursor.clone());
            e.setCursor(null);

            this.sendMessage(player, MessageID.PUTTING_FULL_ITEMS,
                    "player-%-" + player.getName(),
                    "item-%-" + name,
                    "amount-%-" + this.storedItem.getAmount());
        }
        else {
            if (cursor == null) {
                e.setCursor(this.storedItem.clone());
                this.removeStoraged();

                this.sendMessage(player, MessageID.PICKUP_ALL_ITEMS, "player-%-" + player.getName());
                return this;
            }

            if (this.storedItem.isSimilar(cursor)) {
                int maxStack = storedItem.getType().getMaxStackSize();
                int currentAmount = storedItem.getAmount();
                int availableSpace = maxStack - currentAmount;

                if (availableSpace <= 0) {
                    sendMessage(player, MessageID.MAX_STACKED, "player-%-" + player.getName());
                    return this;
                }

                int cursorAmount = cursor.getAmount();
                int toTransfer = Math.min(cursorAmount, availableSpace);

                storedItem.setAmount(currentAmount + toTransfer);
                cursor.setAmount(cursorAmount - toTransfer);

                this.sendMessage(player, MessageID.PUTTING_CONCRETE_VALUE,
                        "player-%-" + player.getName(),
                        "item-%-" + name,
                        "amount-%-" + toTransfer);
            }
            else {
                if (!this.canPutItem(cursor)) {
                    this.sendMessage(player, MessageID.DISABLE_PUTTING, "player-%-" + player.getName(), "item-%-" + name);
                    return this;
                }

                ItemStack tempStack = this.storedItem.clone();
                this.storedItem = cursor.clone();
                e.setCursor(tempStack);

                this.sendMessage(player, MessageID.SWITCH_ITEMS,
                        "player-%-" + player.getName(),
                        "item-%-" + name,
                        "amount-%-" + this.storedItem.getAmount());
            }
        }
        return this;
    }

    protected abstract boolean canPutItem(ItemStack itemStack);
    public abstract StoreItem sendMessage(CommandSender sender, MessageID messageID, String... rpl);

    public StoreItem removeStoraged() {
        return this.setStoredItems(null);
    }

    public StoreItem setStoredItems(@Nullable ItemStack itemStack) {
        this.storedItem = itemStack;
        this.insert();
        return this;
    }

    @Getter
    public enum MessageID {
        NEED_PUT_ITEMS("need_put_items", "player"),
        PUTTING_FULL_ITEMS("put_full_items_in_storage", "player", "item", "amount"),
        PUTTING_CONCRETE_VALUE("put_any_items_in_storage", "player"),
        DISABLE_PUTTING("disable_put_item", "player", "item"),
        MAX_STACKED("storage_is_full", "player"),
        SWITCH_ITEMS("switch_items", "player", "item", "amount"),
        PICKUP_ALL_ITEMS("pickup_all_items", "player");

        private final String messageId;
        private final String[] replacementIds;
        MessageID(String messageId, String... replacementIds) {
            this.messageId = messageId;
            this.replacementIds = replacementIds;
        }
    }
}
