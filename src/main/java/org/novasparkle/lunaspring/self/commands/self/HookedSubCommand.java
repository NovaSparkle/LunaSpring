package org.novasparkle.lunaspring.self.commands.self;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.Comparator;
import java.util.List;

@SubCommand(commandIdentifiers = {"hooked"}, appliedCommand = "lunaspring")
@Check(permissions = {"lunaspring.hooked"}, flags = {})
public class HookedSubCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] args) {
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
