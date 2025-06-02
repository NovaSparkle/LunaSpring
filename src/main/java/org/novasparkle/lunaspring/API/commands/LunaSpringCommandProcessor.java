package org.novasparkle.lunaspring.API.commands;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class LunaSpringCommandProcessor implements TabExecutor {

    private final List<LunaSpringSubCommand> subCommands;
    private final List<String> commandIdentifiers;
    @Accessors(fluent = true)
    private final String appliedCommand;
    private Invocation zeroCommand;

    @SneakyThrows
    public LunaSpringCommandProcessor(@NotNull String appliedCommand) {
        this.subCommands = new ArrayList<>();
        this.commandIdentifiers = new ArrayList<>();
        this.appliedCommand = appliedCommand;
    }

    @Override
    @SneakyThrows
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            for (LunaSpringSubCommand subCommand : this.subCommands) {
                if (!subCommand.hasIdentifier(args[0])) continue;
                subCommand.invoke(sender, args);
                break;
            }
        } else if (this.zeroCommand != null) this.zeroCommand.invoke(sender, args);
          else LSConfig.sendMessage(sender, "wrongArguments");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) return Utils.tabCompleterFiltering(this.commandIdentifiers, args[0]);
        else if (args.length >= 2) {
            LunaSpringSubCommand subCommand = this.subCommands.stream().filter(s -> s.hasIdentifier(args[0])).findFirst().orElse(null);
            if (subCommand != null) return subCommand.tabComplete(sender, List.of(args).subList(1, args.length));
        }
        return List.of();
    }

    public void registerSubCommand(LunaSpringSubCommand subCommand) {
        this.subCommands.add(subCommand);
        this.commandIdentifiers.addAll(subCommand.getCommandIdentifiers());
    }
    public void registerZeroArgCommand(ZeroArgCommand command) {
        this.zeroCommand = command;
    }
}
