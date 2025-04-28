package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.AppliedCommand;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.drops.managers.EventManager;
import org.novasparkle.lunaspring.API.drops.managers.LunaEventManager;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(maxArgs = 2, commandIdentifiers = {"start"}, flags = {})
@AppliedCommand("event")
public class StartSubCommand extends LunaSpringSubCommand {
    public StartSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (!checkCommand(sender, args, "lunaspring.event.start")) return;

        if (args.length == 1) {
            EventManager manager = LunaEventManager.getManagers().stream().findAny().orElse(null);
            if (manager == null || !manager.run()) return;

            LSConfig.sendMessage(sender, "events.startRandom");
            return;
        }

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

        eventManager.run();
        LSConfig.sendMessage(sender, "events.start", "event_name-%-" + eventManager.getName());
    }
}
