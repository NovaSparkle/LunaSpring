package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.Commands.LunaCommand;
import org.novasparkle.lunaspring.API.Commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.Comparator;
import java.util.List;

@LunaCommand(maxArgs = 1, noConsole = false, commandIdentifiers = {"hooked"})
 public class HookedSubCommand extends LunaSpringSubCommand {

    public HookedSubCommand(LunaPlugin plugin, String[] args, int maxArgs, CommandSender sender, boolean noConsole, String[] commandIdentifiers) {
        super(plugin, args, maxArgs, sender, noConsole, commandIdentifiers);
    }

    @Override
    public void invoke() {
        String hooked = LSConfig.getMessage("hooked");
        List<String> pluginNames = LunaSpring.getINSTANCE().getHookedPlugins().stream()
                .map(LunaPlugin::getName)
                .sorted(Comparator.comparingInt(String::length))
                .toList();

        int maxLength = pluginNames.get(pluginNames.size() - 1).length();
        for (String plName : pluginNames) {
            int spaces = (maxLength - plName.length()) / 2;
            String formatted = " ".repeat(spaces) + plName + " ".repeat(spaces);
            this.sender.sendMessage(hooked.replace("[plugin]", formatted));
        }
    }
}
