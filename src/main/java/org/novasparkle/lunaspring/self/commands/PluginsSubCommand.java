package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.novasparkle.lunaspring.API.commands.LunaCommand;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.List;

@LunaCommand(maxArgs = 1, commandIdentifiers = {"pl", "plugins"}, flags = {})
public class PluginsSubCommand extends LunaSpringSubCommand {


    public PluginsSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (checkCommand(sender, args)) return;
        String enabledPlugin = LSConfig.getString("pl_command.enabledPlugin");
        String disabledPlugin = LSConfig.getString("pl_command.disabledPlugin");

        List<String> list = LSConfig.getStringList("plugins");

        for (Plugin plugin : LunaSpring.getINSTANCE().getServer().getPluginManager().getPlugins()) {
            String status = plugin.isEnabled() ? enabledPlugin : disabledPlugin;
            list.forEach(l -> {
                String authors = plugin.getDescription().getAuthors().isEmpty() ? "Не указано" : String.join(", ", plugin.getDescription().getAuthors());
                String line = l
                        .replace("[plugin-name]", plugin.getName())
                        .replace("[plugin-version]", plugin.getDescription().getVersion())
                        .replace("[plugin-authors]", authors)
                        .replace("[status]", status);
                sender.sendMessage(ColorManager.color(line));
            });
        }
    }
}
