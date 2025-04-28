package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.AppliedCommand;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.drops.LunaEvent;
import org.novasparkle.lunaspring.API.drops.managers.EventManager;
import org.novasparkle.lunaspring.API.drops.managers.LunaEventManager;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(maxArgs = 1, commandIdentifiers = {"stop"}, flags = {})
@AppliedCommand("event")
public class StopSubCommand extends LunaSpringSubCommand {
    public StopSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (!checkCommand(sender, args, "lunaspring.event.stop")) return;

        LunaEvent lunaEvent = LunaEventManager.getActiveEvent();
        if (lunaEvent == null) {
            LSConfig.sendMessage(sender, "events.inactive");
            return;
        }

        EventManager eventManager = LunaEventManager.getDropManager(lunaEvent.getLunaPlugin());
        lunaEvent.stop();
        LSConfig.sendMessage(sender, "events.stop", "event_name-%-" + eventManager.getName());
    }
}
