package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.configuration.LSConfig;

import java.util.Comparator;
import java.util.List;

@SubCommand(commandIdentifiers = "hooked", appliedCommand = "lunaspring")
@Check(permissions = "#.hooked", flags = {})
public class HookedSubCommand implements LunaExecutor {
    @Override
    public void invoke(CommandSender sender, String[] args) {
        String hooked = LSConfig.getMessage("hooked");
        List<LunaPlugin> plugins = LunaSpring.getInstance().getHookedPlugins().stream()
                .sorted(Comparator.comparingInt(pl -> pl.getName().length()))
                .toList();

        boolean enableVersionView = args.length == 2 && args[1].equals("-v");
        if (args.length == 2 && args[1].equals("-c")) {
            sender.sendMessage(LSConfig.getMessage("hooked-count").replace("[count]", String.valueOf(plugins.size())));
            return;
        }

        int maxLength = plugins.get(plugins.size() - 1).getName().length();
        for (LunaPlugin lunaPlugin : plugins) {
            int spaces = maxLength - lunaPlugin.getName().length();
            int leftSpaces = spaces / 2;
            int rightSpaces = spaces - leftSpaces;
            String formatted = " ".repeat(leftSpaces) + lunaPlugin.getName() + " ".repeat(rightSpaces);
            String message = hooked.replace("[plugin]", formatted);
            if (enableVersionView) {
                String version = LSConfig.getMessage("version");
                message = message + version.replace("[version]", lunaPlugin.getVersion());
            }
            sender.sendMessage(message);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> args) {
        if (args.size() == 1) return List.of("-v", "-c");
        return null;
    }
}
