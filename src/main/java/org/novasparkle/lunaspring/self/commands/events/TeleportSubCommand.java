package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.AppliedCommand;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.drops.LunaEvent;
import org.novasparkle.lunaspring.API.drops.managers.LunaEventManager;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(maxArgs = 1, commandIdentifiers = {"teleport", "tp"}, flags = {LunaSpringSubCommand.AccessFlag.PLAYER_ONLY})
@AppliedCommand("event")
public class TeleportSubCommand extends LunaSpringSubCommand {
    public TeleportSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (!checkCommand(sender, args, "lunaspring.event.teleport")) return;

        LunaEvent lunaEvent = LunaEventManager.getActiveEvent();
        if (lunaEvent == null) {
            LSConfig.sendMessage(sender, "inactive");
            return;
        }

        Player player = (Player) sender;
        player.teleport(lunaEvent.getLocation().clone().add(0, 1.25, 0));
        LSConfig.sendMessage(player, "events.teleport");
    }
}
