package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(commandIdentifiers = {"reload"}, appliedCommand = "lunaspring")
@Check(permissions = {"lunaspring.reload"}, flags = {})
public class ReloadSubCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] args) {
        LunaSpring.getInstance().reloadConfig();
        LSConfig.reload();
        sender.sendMessage(LSConfig.getMessage("reloaded"));
    }
}
