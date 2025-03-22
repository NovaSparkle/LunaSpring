package org.novasparkle.lunaspring;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.Util.Service.ServiceProvider;
import org.novasparkle.lunaspring.other.LunaPlugin;

import java.util.Objects;

public final class LunaSpring extends LunaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    @Getter
    private static final ServiceProvider serviceProvider = new ServiceProvider();

    public void onEnable() {
        INSTANCE = this;
        this.saveDefaultConfig();
        this.registerCommand(this, "lunaspring");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("lunaspring.reload")) {
            this.reloadConfig();
            sender.sendMessage(Objects.requireNonNull(this.getConfig().getString("messages.reloaded")));
            return true;
        }
        return true;
    }
}



