package org.novasparkle.lunaspring.API.Commands;

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
    private final Set<Class<?>> subCommandsClasses;
    private final List<String> commandIdentifiers;
    private final LunaPlugin mainPluginClass;
    public LunaSpringCommandProcessor(LunaPlugin mainPluginClass) {
        this(mainPluginClass, mainPluginClass.getClass().getPackage());
    }
    public LunaSpringCommandProcessor(LunaPlugin mainPluginClass, Package commadsPackage) {
        this.mainPluginClass = mainPluginClass;
        this.commandIdentifiers = new ArrayList<>();
        Reflections reflections = new Reflections(commadsPackage.getName());
        subCommandsClasses = reflections.getTypesAnnotatedWith(LunaCommand.class);
        subCommandsClasses.forEach(clazz -> {
            LunaCommand lunaCommandAnnotation = clazz.getAnnotation(LunaCommand.class);
            commandIdentifiers.addAll(List.of(lunaCommandAnnotation.commandIdentifiers()));
        });
        System.out.println(subCommandsClasses);
    }

    @Override
    @SneakyThrows
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (Class<?> clazz : subCommandsClasses) {
            LunaCommand annotation = clazz.getAnnotation(LunaCommand.class);
            if (Arrays.stream(annotation.commandIdentifiers()).noneMatch(i -> i.equals(args[1]))) continue;
            Constructor<?> constructor = clazz.getConstructor(
                    LunaPlugin.class,
                    String[].class,
                    Integer.class,
                    CommandSender.class,
                    Boolean.class,
                    String[].class
            );

            LunaSpringSubCommand subCommand = (LunaSpringSubCommand) constructor.newInstance(
                    this.mainPluginClass, args, annotation.maxArgs(),
                    sender, annotation.noConsole(), annotation.commandIdentifiers()
            );
            subCommand.invoke();
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return args.length == 1 ? this.commandIdentifiers : null;
    }
}
