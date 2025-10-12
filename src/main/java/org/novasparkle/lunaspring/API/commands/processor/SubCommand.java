package org.novasparkle.lunaspring.API.commands.processor;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.LunaCompleter;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.List;

@Getter
public class SubCommand extends NoArgCommand {
    private final CommandReq commandRequirements;
    private final List<String> commandIdentifiers;
    private final LunaCompleter tabCompleter;

    @Builder
    public SubCommand(LunaPlugin plugin, String appliedCommand, CommandReq commandReq, String[] commandIdentifiers, Invocation invocation, LunaCompleter tabCompleter) {
        super(plugin, appliedCommand, commandReq.accessFlags(), commandReq.permissions(), invocation);
        this.tabCompleter = tabCompleter;
        this.commandRequirements = commandReq;
        this.commandIdentifiers = List.of(commandIdentifiers);
    }

    public void invoke(CommandSender sender, String[] args) {
        if (this.getPermissions().isEmpty() || this.hasPermission(sender) && checkArgs(sender, args))
            this.getInvocation().invoke(sender, args);
    }

    public List<String> tabComplete(CommandSender sender, List<String> args) {
        if (this.tabCompleter != null) {
            return tabCompleter.tabComplete(sender, args);
        }
        return List.of();
    }

    private boolean checkArgs(CommandSender sender, String[] args) {
        if (this.commandRequirements.maxArgs() != Integer.MAX_VALUE && this.commandRequirements.maxArgs() < args.length) {
            LSConfig.sendMessage(sender, "tooManyArgs");
            return false;

        } else if (this.commandRequirements.minArgs() != Integer.MIN_VALUE && this.commandRequirements.minArgs() > args.length) {
            LSConfig.sendMessage(sender, "tooLowArgs");
            return false;
        }
        return true;
    }

    public boolean hasIdentifier(String inputIdentifier) {
        return this.commandIdentifiers.contains(inputIdentifier);
    }


}
