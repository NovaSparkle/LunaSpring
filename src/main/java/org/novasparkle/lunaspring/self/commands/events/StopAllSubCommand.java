package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.eventManagment.managers.EventManager;
import org.novasparkle.lunaspring.API.eventManagment.managers.LunaEventManager;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(commandIdentifiers = {"stopAll"}, appliedCommand = "event")
@Check(permissions = "lunaspring.event.stop", flags = {})
public class StopAllSubCommand implements Invocation {

    @Override
    public void invoke(CommandSender sender, String[] args) {
        LunaEventManager.getActiveEvents().forEach(e -> {
            EventManager eventManager = LunaEventManager.getManager(e.getLunaPlugin());
            if (eventManager != null) eventManager.stop();
        });
        LSConfig.sendMessage(sender, "events.stopAll");
    }
}
