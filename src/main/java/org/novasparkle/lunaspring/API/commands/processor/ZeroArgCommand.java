package org.novasparkle.lunaspring.API.commands.processor;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.Arrays;
import java.util.List;

@Getter
public class ZeroArgCommand implements Invocation {
    private final LunaPlugin plugin;
    private final String appliedCommand;
    private final List<ZeroArgCommand.AccessFlag> flags;
    private final List<String> permissions;
    private final Invocation invocation;

    @Builder(buildMethodName = "zerobuild", builderMethodName = "zerobuilder")
    public ZeroArgCommand(LunaPlugin lunaPlugin, String appliedCommand, AccessFlag[] flags, String[] permissions, Invocation invocation) {
        this.plugin = lunaPlugin;
        this.appliedCommand = appliedCommand;
        this.flags = List.of(flags);
        this.permissions = Arrays.stream(permissions)
                .map(p -> p.replace("@", plugin.getName().toLowerCase()).replace("#", appliedCommand))
                .toList();
        this.invocation = invocation;
    }

    public void invoke(CommandSender sender, String[] args) {
        if (permissions.isEmpty() || this.hasPermission(sender) && this.invokeFlags(sender))
            this.getInvocation().invoke(sender, args);
    }

    protected final boolean invokeFlags(CommandSender sender) {
        for (AccessFlag flag : this.flags) {
            if (!flag.invoke(sender)) return false;
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

        public boolean invoke(CommandSender sender) {
            if (this.senderClass.isAssignableFrom(sender.getClass()) || sender.getClass().isInstance(this.senderClass)) return true;

            sender.sendMessage(Utils.applyReplacements(LSConfig.getMessage("invalidSender"),
                    "sender-%-" + sender.getClass().getSimpleName()));
            return false;
        }
    }
}