package org.novasparkle.lunaspring.self.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.List;

@SubCommand(commandIdentifiers = {"pl", "plugins"}, appliedCommand = "lunaspring")
@Check(permissions = {"lunaspring.plugins"}, flags = {})
public class PluginsSubCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] args) {

        String enabledPlugin = LSConfig.getString("pl_command.enabledPlugin");
        String disabledPlugin = LSConfig.getString("pl_command.disabledPlugin");

        List<String> list = LSConfig.getStringList("plugins");

        for (Plugin plugin : LunaSpring.getInstance().getServer().getPluginManager().getPlugins()) {
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
