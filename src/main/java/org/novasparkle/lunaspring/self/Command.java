package org.novasparkle.lunaspring.self;

import org.antlr.v4.codegen.model.chunk.ThisRulePropertyRef_parser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.Util.Service.managers.ColorManager;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Command implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1 && hasPermission(sender, "reload")) {
            if (args[0].equalsIgnoreCase("reload")) {
                LunaSpring.getINSTANCE().reloadConfig();
                ConfigManager.reload();
                sender.sendMessage(ConfigManager.getMessage("reloaded"));

            } else if (args[0].equalsIgnoreCase("server-info") && hasPermission(sender, "server-info")) {
                List<String> list = ConfigManager.getStringList("server-info");

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

            } else if (args[0].equalsIgnoreCase("hooked") && hasPermission(sender, "hooked")) {
                String hooked = ConfigManager.getMessage("hooked");
                LunaSpring.getINSTANCE().showHookedPlugins().forEach(pl -> sender.sendMessage(hooked.replace("[plugin]", pl.getName())));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return strings.length == 1 ? List.of("reload", "server-info", "hooked") : Collections.emptyList();
    }
    public static boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(String.format("lunaspring.%s", permission)) &&
                !sender.hasPermission("lunaspring.admin")) {
            sender.sendMessage(ConfigManager.getMessage("noPermission"));
            return false;
        }
        return true;
    }
}
