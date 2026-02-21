package org.novasparkle.lunaspring.API.commands.processor;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.configuration.LSConfig;

import java.util.Arrays;
import java.util.List;

@Getter
public class NoArgCommand implements Invocation {
    private final LunaPlugin plugin;
    private final String appliedCommand;
    private final List<NoArgCommand.AccessFlag> flags;
    private final List<String> permissions;
    private final Invocation invocation;

    @Builder(builderMethodName = "zBuilder", buildMethodName = "zBuild")
    public NoArgCommand(LunaPlugin plugin, String appliedCommand, AccessFlag[] flags, String[] permissions, Invocation invocation) {
        this.plugin = plugin;
        this.appliedCommand = appliedCommand == null ? this.plugin.getName().toLowerCase() : appliedCommand;
        this.flags = Arrays.asList(flags);
        this.invocation = invocation;
        this.permissions = Arrays.stream(permissions)
                .map(p -> p.replace("@", this.plugin.getName().toLowerCase()).replace("#", this.appliedCommand))
                .toList();
    }


    public void invoke(CommandSender sender, String[] args) {
        if (permissions.isEmpty() || this.hasPermission(sender) && this.checkFlags(sender))
            invocation.invoke(sender, args);
    }

    protected final boolean checkFlags(CommandSender sender) {
        for (AccessFlag flag : flags) {
            if (!flag.check(sender)) return false;
        }
        return true;
    }

    protected boolean hasPermission(CommandSender sender) {
        if (permissions.stream().noneMatch(sender::hasPermission) && !sender.hasPermission("lunaspring.*")) {
            sender.sendMessage(LSConfig.getMessage("noPermission"));
            return false;
        }
        return true;
    }

    protected boolean hasPermissionNoMessage(CommandSender sender) {
        return permissions.isEmpty() || permissions.stream().anyMatch(sender::hasPermission) || sender.hasPermission("lunaspring.*");
    }

    public enum AccessFlag {
        PLAYER_ONLY(Player.class),
        CONSOLE_ONLY(ConsoleCommandSender.class);

        private final Class<?> senderClass;
        AccessFlag(Class<?> senderClass) {
            this.senderClass = senderClass;
        }

        public boolean check(CommandSender sender) {
            if (this.senderClass.isAssignableFrom(sender.getClass()) || sender.getClass().isInstance(this.senderClass)) return true;

            sender.sendMessage(Utils.applyReplacements(LSConfig.getMessage("invalidSender"),
                    "sender-%-" + sender.getClass().getSimpleName()));
            return false;
        }
    }
}