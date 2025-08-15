package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(commandIdentifiers = {"version", "-v"}, appliedCommand = "lunaspring")
@Check(permissions = {"lunaspring.version"}, flags = {})
public class VersionSubCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] args) {
        LSConfig.sendMessage(sender, "showVersion", "pluginName-%-" + LunaSpring.getInstance().getName(), "version-%-" + LunaSpring.getInstance().getVersion());
    }
}
