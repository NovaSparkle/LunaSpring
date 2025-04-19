package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.Commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

public class ReloadSubCommand extends LunaSpringSubCommand {
    public ReloadSubCommand(String[] args, CommandSender sender) {
        super(args, 1, sender, false, "reload");
    }

    @Override
    public boolean invoke() {
        if (noPermission()) return true;
        LunaSpring.getINSTANCE().reloadConfig();
        LSConfig.reload();
        this.sender.sendMessage(LSConfig.getMessage("reloaded"));
        return true;
    }
}
