package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.Commands.LunaCommand;
import org.novasparkle.lunaspring.API.Commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

@LunaCommand(maxArgs = 1, noConsole = false, commandIdentifiers = {"reload"})
public class ReloadSubCommand extends LunaSpringSubCommand {
    @Override
    public boolean invoke() {
        if (noPermission()) return true;
        LunaSpring.getINSTANCE().reloadConfig();
        LSConfig.reload();
        this.sender.sendMessage(LSConfig.getMessage("reloaded"));
        return true;
    }
}
