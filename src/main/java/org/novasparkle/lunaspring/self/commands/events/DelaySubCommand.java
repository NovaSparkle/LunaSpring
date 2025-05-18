package org.novasparkle.lunaspring.self.commands.events;


import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.eventManagment.managers.EventManager;
import org.novasparkle.lunaspring.API.eventManagment.managers.LunaEventManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

import java.time.LocalTime;

@SubCommand(commandIdentifiers = {"delay"}, appliedCommand = "event")
@Check(permissions = "lunaspring.event.delay", flags = {})
public class DelaySubCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (args.length == 1) {
            LSConfig.sendMessage(sender, "events.delayNext");
            return;
        }

        if (args.length >= 2) {
            LunaPlugin lunaPlugin = LunaSpring.getINSTANCE().getLunaPlugin(args[1]);
            if (lunaPlugin == null) {
                LSConfig.sendMessage(sender, "events.notExists", "event_name-%-" + args[1]);
                return;
            }

            EventManager eventManager = LunaEventManager.getManager(lunaPlugin);
            if (eventManager == null) {
                LSConfig.sendMessage(sender, "events.notExists", "event_name-%-" + args[1]);
                return;
            }

            if (eventManager.isActive()) {
                LSConfig.sendMessage(sender, "events.isActive", "event_name-%-" + eventManager.getName());
                return;
            }

            LocalTime next = Utils.Time.getNextTime(eventManager.getTimes());
            LSConfig.sendMessage(sender, "events.delay", "event_name-%-" + eventManager.getName(),
                    "event_time-%-" + next.toString(),
                    "event_time_left-%-" + Utils.Time.getTimeBetween(LocalTime.now(), next).toString());
        }
    }
}
