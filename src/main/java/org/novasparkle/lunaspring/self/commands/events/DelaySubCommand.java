package org.novasparkle.lunaspring.self.commands.events;


import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.AppliedCommand;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.drops.managers.EventManager;
import org.novasparkle.lunaspring.API.drops.managers.LunaEventManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

import java.time.LocalTime;

@SubCommand(maxArgs = 2, commandIdentifiers = {"delay"}, flags = {})
@AppliedCommand("event")
public class DelaySubCommand extends LunaSpringSubCommand {
    public DelaySubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (!checkCommand(sender, args, "lunaspring.event.delay")) return;

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

            EventManager eventManager = LunaEventManager.getDropManager(lunaPlugin);
            if (eventManager == null) {
                LSConfig.sendMessage(sender, "events.notExists", "event_name-%-" + args[1]);
                return;
            }

            if (eventManager.isActive()) {
                LSConfig.sendMessage(sender, "events.isActive", "event_name-%-" + eventManager.getName());
                return;
            }

            LocalTime next = Utils.getNextTime(eventManager.getTimes());
            LSConfig.sendMessage(sender, "events.delay", "event_name-%-" + eventManager.getName(),
                    "event_time-%-" + next.toString(),
                    "event_time_left-%-" + Utils.getTimeBetween(LocalTime.now(), next).toString());
        }
    }
}
