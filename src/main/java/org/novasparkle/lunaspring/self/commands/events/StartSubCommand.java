package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.LunaCompleter;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.eventManagment.managers.EventManager;
import org.novasparkle.lunaspring.API.eventManagment.managers.LunaEventManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.ArrayList;
import java.util.List;

@SubCommand(commandIdentifiers = {"start"}, appliedCommand = "event")
@Check(permissions = "lunaspring.event.start", flags = {})
public class StartSubCommand implements LunaCompleter {
    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<EventManager> list = new ArrayList<>(LunaEventManager.getManagers());

            EventManager manager = list.isEmpty() ? null : list.get(LunaMath.getRandomInt(0, list.size()));
            if (manager == null || !manager.run()) return;

            LSConfig.sendMessage(sender, "events.startRandom");
            return;
        }

        LunaPlugin lunaPlugin = LunaSpring.getINSTANCE().getLunaPlugin(args[1]);
        if (lunaPlugin == null) {
            LSConfig.sendMessage(sender, "events.notExists", "event_name-%-" + args[1], args[2]);
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

        LunaEventManager.spawn(eventManager);
        LSConfig.sendMessage(sender, "events.start", "event_name-%-" + eventManager.getName());
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, List<String> args) {
        if (args.size() == 1)
            return Utils.tabCompleterFiltering(LunaEventManager.getManagers()
                    .stream()
                    .filter(m -> !m.isActive())
                    .map(m -> m.getLunaPlugin().getName())
                    .toList(), args.get(0));
        return List.of();
    }
}
