package org.novasparkle.lunaspring.API.Commands;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.List;

@Getter
public abstract class LunaSpringSubCommand {
    private final LunaPlugin plugin;
    private final String[] args;
    private final int maxArgs;
    protected final CommandSender sender;
    protected final List<String> commandIdentifiers;
    public LunaSpringSubCommand(

            LunaPlugin plugin,
            @DynamicValue String[] args,
            @DynamicValue int maxArgs,
            @DynamicValue CommandSender sender,
            boolean noConsole,
            String[] commandIdentifiers
    ) {
        this.plugin = plugin;
        this.args = args;
        this.maxArgs = maxArgs;
        this.sender = sender;
        this.commandIdentifiers = List.of(commandIdentifiers);
        if (noConsole) sender.sendMessage(LSConfig.getMessage("noConsole"));
        if (args.length > maxArgs)
            sender.sendMessage(LSConfig.getMessage("wrongArguments").replace("[maxArgs]", String.valueOf(maxArgs)));
    }

    public boolean checkIdentifier(String inputIdentifier) {
        return this.commandIdentifiers.contains(inputIdentifier);
    }

    public abstract void invoke();
    public boolean noPermission() {
        return !hasPermission(sender, this.getClass().getSimpleName().toLowerCase());
    }

    private static boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(String.format("lunaspring.%s", permission)) &&
                !sender.hasPermission("lunaspring.admin")) {
            sender.sendMessage(LSConfig.getMessage("noPermission"));
            return false;
        }
        return true;
    }
}
