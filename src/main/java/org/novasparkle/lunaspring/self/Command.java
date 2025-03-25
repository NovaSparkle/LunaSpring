package org.novasparkle.lunaspring.self;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.Util.Service.realized.ColorService;
import org.novasparkle.lunaspring.API.Util.managers.ColorManager;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Command implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission("lunaspring.reload")) {
            if (args[0].equalsIgnoreCase("reload")) {
                LunaSpring.getINSTANCE().reloadConfig();
                ConfigManager.reload();
                sender.sendMessage(ConfigManager.getMessage("reloaded"));
            }
            else if (args[0].equalsIgnoreCase("server-info")) {
                List<String> list = ConfigManager.getStringList("messages.server-info");

                int tps = (int) (Arrays.stream(Bukkit.getServer().getTPS()).sum() / Bukkit.getServer().getTPS().length);
                list.forEach(l -> {
                    String line = l
                            .replace("[online]", String.valueOf(Bukkit.getOnlinePlayers().size()))
                            .replace("[players_max]", String.valueOf(Bukkit.getMaxPlayers()))
                            .replace("[bukkit-version]", Bukkit.getBukkitVersion())
                            .replace("[mc-version]", Bukkit.getMinecraftVersion())
                            .replace("[port]", String.valueOf(Bukkit.getPort()))
                            .replace("[average_tps]", String.valueOf(tps));
                    sender.sendMessage(ColorManager.color(line));
                });
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return strings.length == 1 ? List.of("reload", "server-info") : List.of();
    }
}
