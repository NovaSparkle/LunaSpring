package org.novasparkle.lunaspring.self;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.Util.Service.managers.ColorManager;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LSCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            switch (args[0]) {
                case "reload" -> {
                    if (noPermission(sender, "server-info")) return true;
                    LunaSpring.getINSTANCE().reloadConfig();
                    LSConfig.reload();
                    sender.sendMessage(LSConfig.getMessage("reloaded"));

                } case "server-info" -> {
                    List<String> list = LSConfig.getStringList("server-info");

                    int tps = (int) (Arrays.stream(Bukkit.getServer().getTPS()).sum() / Bukkit.getServer().getTPS().length);
                    list.forEach(l -> {
                        String line = l
                                .replace("[online]", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replace("[max-players]", String.valueOf(Bukkit.getMaxPlayers()))
                                .replace("[bukkit-version]", Bukkit.getBukkitVersion())
                                .replace("[mc-version]", Bukkit.getMinecraftVersion())
                                .replace("[port]", String.valueOf(Bukkit.getPort()))
                                .replace("[average_tps]", String.valueOf(tps));
                        sender.sendMessage(ColorManager.color(line));
                    });

                } case "pl", "plugins" -> {
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
                                    .replace("[plugin-authors]", Bukkit.getBukkitVersion())
                                    .replace("[status]", status);
                            sender.sendMessage(ColorManager.color(line));
                        });
                    }

                } case "hooked" -> {
                    String hooked = LSConfig.getMessage("hooked");
                    LunaSpring.getINSTANCE().getHookedPlugins().forEach(pl -> sender.sendMessage(hooked.replace("[plugin]", pl.getName())));

                } default -> sender.sendMessage(LSConfig.getMessage("undefinedCommand"));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return strings.length == 1 ? List.of("reload", "server-info", "hooked", "pl") : Collections.emptyList();
    }
    private static boolean noPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(String.format("lunaspring.%s", permission)) &&
                !sender.hasPermission("lunaspring.admin")) {
            sender.sendMessage(LSConfig.getMessage("noPermission"));
            return true;
        }
        return false;
    }
}
