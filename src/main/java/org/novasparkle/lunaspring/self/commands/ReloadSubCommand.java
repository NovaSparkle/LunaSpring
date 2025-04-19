package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.Commands.LunaCommand;
import org.novasparkle.lunaspring.API.Commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

@LunaCommand(maxArgs = 1, noConsole = false, commandIdentifiers = {"reload"})
public class ReloadSubCommand extends LunaSpringSubCommand {
    public ReloadSubCommand(LunaPlugin plugin, String[] args, int maxArgs, CommandSender sender, boolean noConsole, String[] commandIdentifiers) {
        super(plugin, args, maxArgs, sender, noConsole, commandIdentifiers);
    }

    @Override
    public void invoke() {
        if (noPermission()) return;
        LunaSpring.getINSTANCE().reloadConfig();
        LSConfig.reload();
        this.sender.sendMessage(LSConfig.getMessage("reloaded"));
    }
}
