package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.drops.managers.EventManager;
import org.novasparkle.lunaspring.API.drops.managers.LunaEventManager;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(maxArgs = 1, commandIdentifiers = {"stopAll"}, flags = {})
public class StopAllSubCommand extends LunaSpringSubCommand {
    public StopAllSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (!checkCommand(sender, args, "lunaspring.event.stop")) return;
        LunaEventManager.getActiveEvents().forEach(e -> {
            EventManager eventManager = LunaEventManager.getManager(e.getLunaPlugin());
            if (eventManager != null) eventManager.stop();
        });
        LSConfig.sendMessage(sender, "events.stopAll");
    }
}
