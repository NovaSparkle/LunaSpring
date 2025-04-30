package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.eventManagment.LunaEvent;
import org.novasparkle.lunaspring.API.eventManagment.managers.LunaEventManager;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(commandIdentifiers = {"teleport", "tp"}, appliedCommand = "event")
@Check(permissions = "lunaspring.event.teleport", flags = LunaSpringSubCommand.AccessFlag.PLAYER_ONLY)
public class TeleportSubCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] args) {
        LunaEvent lunaEvent = LunaEventManager.getActiveEvent();
        if (lunaEvent == null) {
            LSConfig.sendMessage(sender, "events.inactive");
            return;
        }

        Player player = (Player) sender;
        player.teleport(lunaEvent.getLocation().clone().add(0, 1.25, 0));
        LSConfig.sendMessage(player, "events.teleport");
    }
}
