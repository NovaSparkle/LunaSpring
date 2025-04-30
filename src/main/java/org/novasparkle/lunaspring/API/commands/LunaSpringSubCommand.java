package org.novasparkle.lunaspring.API.commands;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.List;

@Getter
public abstract class LunaSpringSubCommand {
    private final LunaPlugin plugin;
    private final int maxArgs;
    private final List<String> commandIdentifiers;
    private final List<AccessFlag> flags;
    public LunaSpringSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        this.plugin = plugin;
        this.maxArgs = maxArgs;
        this.commandIdentifiers = List.of(commandIdentifiers);
        this.flags = List.of(flags);
    }

    public boolean invalidArgsAmount(CommandSender sender, String[] args) {
        if (args.length > this.maxArgs) {
            sender.sendMessage(Utils.applyReplacements(LSConfig.getMessage("wrongArguments"), "maxArgs-%-" + this.maxArgs));
            return true;
        }
        return false;
    }

    public boolean hasIdentifier(String inputIdentifier) {
        return this.commandIdentifiers.contains(inputIdentifier);
    }

    public abstract void invoke(CommandSender sender, String[] args);

    public final boolean invokeFlags(CommandSender sender) {
        for (AccessFlag flag : this.flags) {
            if (!flag.invoke(sender)) return false;
        }
        return true;
    }

    public boolean checkCommand(CommandSender sender, String[] args, String permission) {
        return hasPermission(sender, permission) && checkCommand(sender, args);
    }

    public boolean checkCommand(CommandSender sender, String[] args) {
        return !invalidArgsAmount(sender, args) && invokeFlags(sender);
    }

    private static boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission) && !sender.hasPermission("lunaspring.*")) {
            sender.sendMessage(LSConfig.getMessage("noPermission"));
            return false;
        }
        return true;
    }

    public List<String> tabComplete(CommandSender sender, List<String> subCommandArgs) {
        return List.of();
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
