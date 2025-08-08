package org.novasparkle.lunaspring.API.commands;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.commands.annotations.*;
import org.novasparkle.lunaspring.API.util.exceptions.InvalidImplementationException;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.API.util.utilities.reflection.AnnotationScanner;
import org.novasparkle.lunaspring.API.util.utilities.reflection.ClassEntry;
import org.novasparkle.lunaspring.LunaPlugin;

import java.util.List;
import java.util.Set;

@UtilityClass
public final class LunaExecutor {
    @SneakyThrows
    public void initialize(LunaPlugin plugin, String... allowedPackages) {
        Set<ClassEntry<SubCommand>> classList = AnnotationScanner.findAnnotatedClasses(plugin, SubCommand.class, allowedPackages);
        Set<String> commands = plugin.getDescription().getCommands().keySet();

        Set<ClassEntry<ZeroArgSubCommand>> zeroArgSubCommandsList = AnnotationScanner.findAnnotatedClasses(plugin, ZeroArgSubCommand.class, allowedPackages);

        for (String command : commands) {
            LunaSpringCommandProcessor processor = new LunaSpringCommandProcessor(command);

            ClassEntry<ZeroArgSubCommand> zeroArgSubCommandClassEntry = Utils.find(zeroArgSubCommandsList, zac -> zac.getAnnotation().appliedCommand().equals(command)).orElse(null);
            if (zeroArgSubCommandClassEntry != null) {
                processZeroArgsCommand(zeroArgSubCommandClassEntry, processor);
            }

            for (ClassEntry<SubCommand> classEntry : classList) {
                processSubCommand(classEntry, command, processor);
            }

            if (zeroArgSubCommandClassEntry != null || !classList.isEmpty())
                plugin.registerCommandProcessor(processor);
        }
    }

    @SneakyThrows
    public void processZeroArgsCommand(ClassEntry<ZeroArgSubCommand> entry, LunaSpringCommandProcessor processor) {
        Class<?> clazz = entry.getClazz();
        if (!Invocation.class.isAssignableFrom(clazz)) throw new InvalidImplementationException(clazz, Invocation.class);

        String[] permissions = new String[] { };
        LunaSpringSubCommand.AccessFlag[] flags = new LunaSpringSubCommand.AccessFlag[] { };


        Check checkAnnotation = (Check) entry.getAdditionalAnnotations().stream().filter(a -> a.annotationType().equals(Check.class)).findFirst().orElse(null);
        if (checkAnnotation != null) {
            permissions = checkAnnotation.permissions();
            flags = checkAnnotation.flags();

        } else {
            Permissions permissionsAnnotation = (Permissions) Utils.find(entry.getAdditionalAnnotations(), a -> a.annotationType().equals(Permissions.class)).orElse(null);
            if (permissionsAnnotation != null) {
                permissions = permissionsAnnotation.permissionList();
            }
            Flags flagsAnnotation = (Flags) Utils.find(entry.getAdditionalAnnotations(), a -> a.annotationType().equals(Flags.class)).orElse(null);
            if (flagsAnnotation != null) {
                flags = flagsAnnotation.flagList();
            }
        }

        processor.registerZeroArgCommand(new ZeroArgCommand(flags, permissions, (Invocation) clazz.getDeclaredConstructor().newInstance()));
    }

    @SneakyThrows
    public void processSubCommand(ClassEntry<SubCommand> classEntry, String command, LunaSpringCommandProcessor processor) {
        SubCommand subCommandAnnotation = classEntry.getAnnotation();

        if (subCommandAnnotation.appliedCommand().equals(command)) {
            Class<?> clazz = classEntry.getClazz();

            if (!Invocation.class.isAssignableFrom(clazz))
                throw new InvalidImplementationException(clazz, Invocation.class);

            Check checkAnnotation = (Check) classEntry.getAdditionalAnnotations().stream().filter(a -> a.annotationType().equals(Check.class)).findFirst().orElse(null);
            String[] permissions = new String[] { };
            LunaSpringSubCommand.AccessFlag[] flags = new ZeroArgCommand.AccessFlag[] { };
            String[] ignoreTabCompleting = new String[] { };
            int maxArgs = Integer.MAX_VALUE;
            int minArgs = Integer.MAX_VALUE;

            if (checkAnnotation != null) {
                permissions = checkAnnotation.permissions();
                flags = checkAnnotation.flags();
            } else {
                Permissions permissionsAnnotation = (Permissions) Utils.find(classEntry.getAdditionalAnnotations(), a -> a.annotationType().equals(Permissions.class)).orElse(null);
                if (permissionsAnnotation != null) {
                    permissions = permissionsAnnotation.permissionList();
                }
                Flags flagsAnnotation = (Flags) Utils.find(classEntry.getAdditionalAnnotations(), a -> a.annotationType().equals(Flags.class)).orElse(null);
                if (flagsAnnotation != null) {
                    flags = flagsAnnotation.flagList();
                }
            }
            Args argsAnnotation = (Args) Utils.find(classEntry.getAdditionalAnnotations(), a -> a.annotationType().equals(Args.class)).orElse(null);
            if (argsAnnotation != null) {
                maxArgs = argsAnnotation.max();
                minArgs = argsAnnotation.min();
            }
            TabCompleteIgnore tabCompleteIgnore = (TabCompleteIgnore) Utils.find(classEntry.getAdditionalAnnotations(), a -> a.annotationType().equals(TabCompleteIgnore.class)).orElse(null);
            if (tabCompleteIgnore != null) {
                if (tabCompleteIgnore.ignoreList().length == 0) {
                    ignoreTabCompleting = subCommandAnnotation.commandIdentifiers();
                } else
                    ignoreTabCompleting = tabCompleteIgnore.ignoreList();
            }

            Invocation commandInstance = (Invocation) clazz.getDeclaredConstructor().newInstance();

            CommandReq commandReq = new CommandReq(permissions, flags, List.of(ignoreTabCompleting), maxArgs, minArgs);

            LunaSpringSubCommand subCommand = new LunaSpringSubCommand(
                    commandReq,
                    subCommandAnnotation.commandIdentifiers(),
                    commandInstance);

            if (LunaCompleter.class.isAssignableFrom(clazz)) {
                LunaCompleter lunaCompleter = ((LunaCompleter) commandInstance);
                subCommand.setTabCompleter(lunaCompleter);
            }

            processor.registerSubCommand(subCommand);
        }

    }
}
