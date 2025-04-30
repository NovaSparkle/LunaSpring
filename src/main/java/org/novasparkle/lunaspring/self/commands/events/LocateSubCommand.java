package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.eventManagment.LunaEvent;
import org.novasparkle.lunaspring.API.eventManagment.managers.LunaEventManager;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(commandIdentifiers = {"locate"}, appliedCommand = "event")
@Check(permissions = "lunaspring.event.locate", flags = {})
public class LocateSubCommand implements Invocation {

    @Override
    public void invoke(CommandSender sender, String[] args) {
        LunaEvent lunaEvent = LunaEventManager.getActiveEvent();
        if (lunaEvent == null) {
            LSConfig.sendMessage(sender, "events.inactive");
            return;
        }
        LSConfig.sendMessage(sender, "events.locate");
    }
}
