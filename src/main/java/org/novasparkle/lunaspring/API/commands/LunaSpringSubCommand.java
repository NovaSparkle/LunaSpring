package org.novasparkle.lunaspring.API.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.List;

@Getter
public class LunaSpringSubCommand implements LunaCompleter {

    private final LunaPlugin plugin;
    private final List<String> commandIdentifiers;
    private final List<AccessFlag> flags;
    private final List<String> permissions;
    private final Invocation invocation;
    @Setter
    private LunaCompleter tabCompleter;

    public LunaSpringSubCommand(LunaPlugin plugin,
                                String[] commandIdentifiers,
                                AccessFlag[] flags,
                                String[] permissions, Invocation invocation) {

        this.plugin = plugin;
        this.commandIdentifiers = List.of(commandIdentifiers);
        this.flags = List.of(flags);
        this.permissions = List.of(permissions);

        this.invocation = invocation;
    }

    public boolean hasIdentifier(String inputIdentifier) {
        return this.commandIdentifiers.contains(inputIdentifier);
    }

    public final boolean invokeFlags(CommandSender sender) {
        for (AccessFlag flag : this.flags) {
            if (!flag.invoke(sender)) return false;
        }
        return true;
    }

    public boolean checkCommand(CommandSender sender, String permission) {
        return hasPermission(sender, permission) && invokeFlags(sender);
    }
    public boolean checkCommand(CommandSender sender, List<String> permission) {
        return permission.stream().allMatch(p -> hasPermission(sender, p)) && invokeFlags(sender);
    }

    private static boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission) && !sender.hasPermission("lunaspring.*")) {
            sender.sendMessage(LSConfig.getMessage("noPermission"));
            return false;
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> subCommandArgs) {
        if (this.tabCompleter != null) {
            tabCompleter.tabComplete(sender, subCommandArgs);
        }
        return List.of();
    }


    public void invoke(CommandSender sender, String[] args) {
        if (this.checkCommand(sender, this.permissions))
            invocation.invoke(sender, args);
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
