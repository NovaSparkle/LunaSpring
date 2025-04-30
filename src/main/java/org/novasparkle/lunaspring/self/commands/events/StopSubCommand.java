package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.eventManagment.LunaEvent;
import org.novasparkle.lunaspring.API.eventManagment.managers.EventManager;
import org.novasparkle.lunaspring.API.eventManagment.managers.LunaEventManager;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(commandIdentifiers = {"stop"}, appliedCommand = "event")
@Check(permissions = "lunaspring.event.stop", flags = {})
public class StopSubCommand implements Invocation {

    @Override
    public void invoke(CommandSender sender, String[] args) {
        LunaEvent activeEvent = LunaEventManager.getActiveEvent();
        EventManager eventManager = args.length == 1 ? (activeEvent == null ? null : LunaEventManager.getManager(activeEvent.getLunaPlugin())) :
                LunaEventManager.getManager(LunaSpring.getINSTANCE().getLunaPlugin(args[1]));

        if (eventManager == null || !eventManager.isActive()) {
            LSConfig.sendMessage(sender, "events.inactive");
            return;
        }

        LunaEventManager.stop(eventManager);
        LSConfig.sendMessage(sender, "events.stop", "event_name-%-" + eventManager.getName());
    }
}
