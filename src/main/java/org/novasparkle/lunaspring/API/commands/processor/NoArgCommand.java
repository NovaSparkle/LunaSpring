package org.novasparkle.lunaspring.API.commands.processor;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.LunaCompleter;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class NoArgCommand implements LunaExecutor {
    private final LunaPlugin plugin;
    private final String appliedCommand;
    private final List<NoArgCommand.AccessFlag> flags;
    private final List<String> permissions;

    private final Invocation invocation;
    private LunaCompleter tabCompleter;

    public NoArgCommand(LunaPlugin lunaPlugin, String appliedCommand, AccessFlag[] flags, String[] permissions, Invocation invocation, LunaCompleter tabCompleter) {
        this.plugin = lunaPlugin;
        this.appliedCommand = appliedCommand == null ? plugin.getName().toLowerCase() : appliedCommand;
        this.flags = List.of(flags);
        this.invocation = invocation;
        this.permissions = Arrays.stream(permissions)
                .map(p -> p.replace("@", plugin.getName().toLowerCase()).replace("#", this.appliedCommand))
                .toList();
    }


    public void invoke(CommandSender sender, String[] args) {
        if (permissions.isEmpty() || this.hasPermission(sender) && this.checkFlags(sender))
            invocation.invoke(sender, args);
    }

    public List<String> tabComplete(CommandSender sender, List<String> args) {
        if (this.tabCompleter != null) {
            return tabCompleter.tabComplete(sender, args);
        }
        return List.of();
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