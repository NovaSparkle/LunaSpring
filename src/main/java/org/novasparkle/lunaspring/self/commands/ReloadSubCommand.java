package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaCommand;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

@LunaCommand(maxArgs = 1, commandIdentifiers = {"reload"}, flags = {})
public class ReloadSubCommand extends LunaSpringSubCommand {

    public ReloadSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (checkCommand(sender, args)) return;
        LunaSpring.getINSTANCE().reloadConfig();
        LSConfig.reload();
        sender.sendMessage(LSConfig.getMessage("reloaded"));
    }
}
