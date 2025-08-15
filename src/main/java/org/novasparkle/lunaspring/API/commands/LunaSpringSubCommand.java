package org.novasparkle.lunaspring.API.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.List;

@Getter
public class LunaSpringSubCommand extends ZeroArgCommand implements LunaCompleter {
    private final CommandReq commandRequirements;

    private final List<String> commandIdentifiers;
    @Setter
    private LunaCompleter tabCompleter;

    public LunaSpringSubCommand(CommandReq commandReq, String[] commandIdentifiers, Invocation invocation) {
        super(commandReq.accessFlags(), commandReq.permissions(), invocation);

        this.commandRequirements = commandReq;
        this.commandIdentifiers = List.of(commandIdentifiers);
    }


    public void invoke(CommandSender sender, String[] args) {
        if (this.getPermissions().isEmpty() || this.hasPermission(sender, this.getPermissions()) && checkArgs(sender, args))
            this.getInvocation().invoke(sender, args);
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

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> args) {
        if (this.tabCompleter != null) {
            return tabCompleter.tabComplete(sender, args);
        }
        return List.of();
    }
}
