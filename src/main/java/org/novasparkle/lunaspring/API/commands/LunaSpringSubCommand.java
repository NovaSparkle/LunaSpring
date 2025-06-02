package org.novasparkle.lunaspring.API.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

import java.util.List;

@Getter
public class LunaSpringSubCommand extends ZeroArgCommand implements LunaCompleter {

    private final List<String> commandIdentifiers;
    @Setter
    private LunaCompleter tabCompleter;

    public LunaSpringSubCommand(String[] commandIdentifiers,
                                ZeroArgCommand.AccessFlag[] flags,
                                String[] permissions, Invocation invocation) {
        super(flags, permissions, invocation);

        this.commandIdentifiers = List.of(commandIdentifiers);
    }

    public boolean hasIdentifier(String inputIdentifier) {
        return this.commandIdentifiers.contains(inputIdentifier);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> subCommandArgs) {
        if (this.tabCompleter != null) {
            return tabCompleter.tabComplete(sender, subCommandArgs);
        }
        return List.of();
    }
}
