package org.novasparkle.lunaspring;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.Events.MenuHandler;
import org.novasparkle.lunaspring.Util.Service.ServiceProvider;
import org.novasparkle.lunaspring.Util.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class LunaSpring extends JavaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    @Getter
    private static final ServiceProvider serviceProvider = new ServiceProvider();
    @Getter
    private static JavaPlugin plugin = null;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("lunaspring.reload")) {
            this.reloadConfig();
            sender.sendMessage(Objects.requireNonNull(this.getConfig().getString("messages.reloaded")));
            return true;
        }
        return true;
    }

    public void onEnable() {
        INSTANCE = this;
        this.saveDefaultConfig();
        Objects.requireNonNull(this.getCommand("lunaspring")).setExecutor(this);
    }

    public static MenuHandler initialize(JavaPlugin plugin) {
        LunaSpring.plugin = plugin;
        List<String> startMessage = Arrays.asList(
                "",
                "    &b&l[pluginName] &1v[pluginVersion]",
                "        &cEngined with &c&nLunaSpring v[LSVersion]",
                "        &8Author: &bNova Sparkle",
                "        &8Dev-Helper: &dProGiple",
                ""
        );
        startMessage.replaceAll(m -> m.replace("[pluginName]", plugin.getName()).replace("[pluginVersion]", plugin.getDescription().getVersion()).replace("[LSVersion]", INSTANCE.getDescription().getVersion()));
        startMessage.forEach(Utils::info);
        return new MenuHandler();
    }
}



