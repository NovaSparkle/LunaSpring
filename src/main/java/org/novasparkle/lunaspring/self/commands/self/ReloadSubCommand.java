package org.novasparkle.lunaspring.self.commands.self;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.annotations.AppliedCommand;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(maxArgs = 1, commandIdentifiers = {"reload"}, flags = {})
@AppliedCommand("lunaspring")
public class ReloadSubCommand extends LunaSpringSubCommand {

    public ReloadSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (checkCommand(sender, args, "lunaspring.reload")) {
            LunaSpring.getINSTANCE().reloadConfig();
            LSConfig.reload();
            sender.sendMessage(LSConfig.getMessage("reloaded"));
        }
    }
}
