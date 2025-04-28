package org.novasparkle.lunaspring.API.commands;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.commands.annotations.AppliedCommand;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.util.utilities.reflection.AnnotationScanner;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public final class LunaSpringCommandProcessor implements TabExecutor {
    private final List<LunaSpringSubCommand> subCommands;
    private final List<String> commandIdentifiers;
    private final LunaPlugin mainPluginClass;
    @Accessors(fluent = true)
    @Getter
    private final String appliedCommand;

    @SneakyThrows
    public LunaSpringCommandProcessor(LunaPlugin mainPluginClass, String appliedCommand) {
        this.mainPluginClass = mainPluginClass;
        this.subCommands = new ArrayList<>();
        this.commandIdentifiers = new ArrayList<>();
        this.appliedCommand = appliedCommand;
        
        AnnotationScanner scanner = new AnnotationScanner();
        List<Class<?>> subCommandsClasses = scanner.getAnnotatedClasses(mainPluginClass, SubCommand.class);
        String joined = subCommandsClasses.stream().map(Class::getSimpleName).collect(Collectors.joining(", "));
        this.mainPluginClass.info(LSConfig.getMessage("subCommandClasses")
                .replace("[amount]", String.valueOf(subCommandsClasses.size()))
                .replace("[commands]", joined).replace("[package]", mainPluginClass.getClass().getPackage().getName()));

        for (Class<?> clazz : subCommandsClasses) {
            AppliedCommand command = clazz.getAnnotation(AppliedCommand.class);
            if (!command.value().equals(this.appliedCommand)) continue;

            SubCommand scAnnotation = clazz.getAnnotation(SubCommand.class);
            Constructor<?> constructor = clazz.getDeclaredConstructor(
                    LunaPlugin.class,                           // LunaPlugin
                    int.class,                                 // maxArgs
                    String[].class,                           // commandIdentifiers
                    LunaSpringSubCommand.AccessFlag[].class  // AccessFlags
            );

            LunaSpringSubCommand subCommand = (LunaSpringSubCommand) constructor.newInstance(
                    this.mainPluginClass, scAnnotation.maxArgs(), scAnnotation.commandIdentifiers(), scAnnotation.flags()
            );
            this.subCommands.add(subCommand);
            this.commandIdentifiers.addAll(subCommand.getCommandIdentifiers());
        }
    }

    @Override
    @SneakyThrows
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (LunaSpringSubCommand subCommand : this.subCommands) {
            if (!subCommand.hasIdentifier(args[0])) continue;
            subCommand.invoke(sender, args);
            break;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return args.length == 1 ? this.commandIdentifiers : null;
    }
}
