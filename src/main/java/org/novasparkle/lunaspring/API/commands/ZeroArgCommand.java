package org.novasparkle.lunaspring.API.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.List;

@Getter
public class ZeroArgCommand implements Invocation {
    private final List<ZeroArgCommand.AccessFlag> flags;
    private final List<String> permissions;
    private final Invocation invocation;
    @Setter
    private String appliedCommand;

    public ZeroArgCommand(AccessFlag[] flags, String[] permissions, Invocation invocation) {
        this.flags = List.of(flags);
        this.permissions = List.of(permissions);
        this.invocation = invocation;
    }

    public void invoke(CommandSender sender, String[] args) {
        if (permissions.isEmpty() || this.hasPermission(sender, permissions) && this.invokeFlags(sender))
            this.getInvocation().invoke(sender, args);
    }

    protected final boolean invokeFlags(CommandSender sender) {
        for (AccessFlag flag : this.flags) {
            if (!flag.invoke(sender)) return false;
        }
        return true;
    }

    protected boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission.replace("#", appliedCommand)) && !sender.hasPermission("lunaspring.*")) {
            sender.sendMessage(LSConfig.getMessage("noPermission"));
            return false;
        }
        return true;
    }
    protected boolean hasPermission(CommandSender sender, List<String> permissions) {
        if (permissions.stream().map(perm -> perm.replace("#", appliedCommand)).noneMatch(sender::hasPermission) && !sender.hasPermission("lunaspring.*")) {
            sender.sendMessage(LSConfig.getMessage("noPermission"));
            return false;
        }
        return true;
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