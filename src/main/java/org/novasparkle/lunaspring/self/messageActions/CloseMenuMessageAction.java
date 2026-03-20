package org.novasparkle.lunaspring.self.messageActions;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.novasparkle.lunaspring.API.messageActions.AbsMessageAction;
import org.novasparkle.lunaspring.API.messageActions.MessageAction;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaSpring;

@MessageAction("CLOSE_MENU")
public class CloseMenuMessageAction extends AbsMessageAction<HumanEntity> {
    @Override
    public void execute(HumanEntity target, String line) {
        InventoryCloseEvent.Reason reason;
        if (line != null && !line.isEmpty()) {
            reason = Utils.getEnumValue(InventoryCloseEvent.Reason.class, line, InventoryCloseEvent.Reason.PLUGIN);
        }
        else
            reason = InventoryCloseEvent.Reason.PLUGIN;

        Bukkit.getScheduler().runTask(LunaSpring.getInstance(), () -> {
            target.closeInventory(reason);
        });
    }

    @Override
    public boolean canCast(CommandSender sender) {
        return sender instanceof HumanEntity;
    }
}
