package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.eventManagment.LunaEvent;
import org.novasparkle.lunaspring.API.eventManagment.managers.LunaEventManager;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(commandIdentifiers = {"compass"}, appliedCommand = "event")
@Check(permissions = "lunaspring.event.compass", flags = LunaSpringSubCommand.AccessFlag.PLAYER_ONLY)

public class CompassSubCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() != Material.COMPASS) {
            LSConfig.sendMessage(player, "events.noCompass");
            return;
        }

        LunaEvent lunaEvent = LunaEventManager.getActiveEvent();
        if (lunaEvent == null) {
            LSConfig.sendMessage(sender, "events.inactive");
            return;
        }

        player.setCompassTarget(lunaEvent.getLocation());
        LSConfig.sendMessage(player, "events.compass");
    }
}
