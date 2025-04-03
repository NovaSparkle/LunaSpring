package org.novasparkle.lunaspring.self;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.Util.Service.managers.ColorManager;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.List;

import static org.novasparkle.lunaspring.self.LSCommand.noPermission;

public class PluginsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (!LSConfig.getBoolean("custom_pl_command.enabled") || noPermission(sender, "plugins")) return true;
            String enabledPlugin = LSConfig.getString("custom_pl_command.enabledPlugin");
            String disabledPlugin = LSConfig.getString("custom_pl_command.disabledPlugin");

            List<String> list = LSConfig.getStringList("plugins");

            for (Plugin plugin : LunaSpring.getINSTANCE().getServer().getPluginManager().getPlugins()) {
                String status = plugin.isEnabled() ? enabledPlugin : disabledPlugin;
                list.forEach(l -> {
                    String line = l
                            .replace("[plugin-name]", plugin.getName())
                            .replace("[plugin-version]", plugin.getDescription().getVersion())
                            .replace("[plugin-authors]", String.join(", ", plugin.getDescription().getAuthors()))
                            .replace("[status]", status);
                    sender.sendMessage(ColorManager.color(line));
                });
            }
            return true;
        }
        return true;
    }
}
