package org.novasparkle.lunaspring.self.commands.self;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.AppliedCommand;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.Comparator;
import java.util.List;

@SubCommand(maxArgs = 1, commandIdentifiers = {"hooked"}, flags = {})
@AppliedCommand("lunaspring")
public class HookedSubCommand extends LunaSpringSubCommand {

    public HookedSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (checkCommand(sender, args, "lunaspring.hooked")) {
            String hooked = LSConfig.getMessage("hooked");
            List<String> pluginNames = LunaSpring.getINSTANCE().getHookedPlugins().stream()
                    .map(LunaPlugin::getName)
                    .sorted(Comparator.comparingInt(String::length))
                    .toList();
            int maxLength = pluginNames.get(pluginNames.size() - 1).length();
            for (String plName : pluginNames) {
                int spaces = (maxLength - plName.length()) / 2;
                String formatted = " ".repeat(spaces) + plName + " ".repeat(spaces);
                sender.sendMessage(hooked.replace("[plugin]", formatted));
            }
        }
    }
}
