package org.novasparkle.lunaspring.API.commands;

import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.LunaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.*;

public final class LunaSpringCommandProcessor implements TabExecutor {
    private final List<LunaSpringSubCommand> subCommands;
    private final List<String> commandIdentifiers;
    private final LunaPlugin mainPluginClass;
    public LunaSpringCommandProcessor(LunaPlugin mainPluginClass) {
        this(mainPluginClass, mainPluginClass.getClass().getPackage());
    }
    @SneakyThrows
    public LunaSpringCommandProcessor(LunaPlugin mainPluginClass, Package commadsPackage) {
        this.mainPluginClass = mainPluginClass;
        this.subCommands = new ArrayList<>();
        this.commandIdentifiers = new ArrayList<>();
        
        Reflections reflections = new Reflections(commadsPackage.getName());
        Set<Class<?>> subCommandsClasses = reflections.getTypesAnnotatedWith(LunaCommand.class);
        for (Class<?> clazz : subCommandsClasses) {
            LunaCommand annotation = clazz.getAnnotation(LunaCommand.class);
            Constructor<?> constructor = clazz.getDeclaredConstructor(
                    LunaPlugin.class,                         // LunaPlugin
                    int.class,                               // maxArgs
                    String[].class,                         // commandIdentifiers
                    LunaSpringSubCommand.AccessFlag[].class  // AccessFlags
            );
            LunaSpringSubCommand subCommand = (LunaSpringSubCommand) constructor.newInstance(
                    this.mainPluginClass, annotation.maxArgs(), annotation.commandIdentifiers(), annotation.flags()
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
