package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.novasparkle.lunaspring.API.Commands.LunaCommand;
import org.novasparkle.lunaspring.API.Commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.Util.Service.managers.ColorManager;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.List;

@LunaCommand(maxArgs = 1, noConsole = false, commandIdentifiers = {"pl", "plugins"})
public class PluginsSubCommand extends LunaSpringSubCommand {

    @Override
    public boolean invoke() {
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
                this.sender.sendMessage(ColorManager.color(line));
            });
        }
        return true;
    }
}
