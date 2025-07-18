package org.novasparkle.lunaspring.API.commands;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.ZeroArgSubCommand;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.API.configuration.builder.ConfigConstructor;
import org.novasparkle.lunaspring.API.util.exceptions.InvalidImplementationException;
import org.novasparkle.lunaspring.API.util.utilities.reflection.AnnotationScanner;
import org.novasparkle.lunaspring.API.util.utilities.reflection.ClassEntry;
import org.novasparkle.lunaspring.LunaPlugin;

import java.util.Set;

@UtilityClass
public final class LunaExecutor {
    @SneakyThrows
    public void initialize(LunaPlugin plugin) {
        Set<ClassEntry<SubCommand>> classList = AnnotationScanner.findAnnotatedClasses(plugin, SubCommand.class);
        Set<String> commands = plugin.getDescription().getCommands().keySet();

        Set<ClassEntry<ZeroArgSubCommand>> zeroArgSubCommandsList = AnnotationScanner.findAnnotatedClasses(plugin, ZeroArgSubCommand.class);
        for (String command : commands) {
            LunaSpringCommandProcessor processor = new LunaSpringCommandProcessor(command);

            ClassEntry<ZeroArgSubCommand> zeroArgSubCommandClassEntry = zeroArgSubCommandsList.stream().filter(zac -> zac.getAnnotation().appliedCommand().equals(command)).findFirst().orElse(null);
            if (zeroArgSubCommandClassEntry != null) {
                Check checkAnnotation = (Check) zeroArgSubCommandClassEntry.getAdditionalAnnotations().stream().filter(a -> a.annotationType().equals(Check.class)).findFirst().orElse(null);

                String[] permissions = new String[] { };
                LunaSpringSubCommand.AccessFlag[] flags = new LunaSpringSubCommand.AccessFlag[] { };

                Class<?> clazz = zeroArgSubCommandClassEntry.getClazz();
                if (!Invocation.class.isAssignableFrom(clazz))
                    throw new InvalidImplementationException(clazz, Invocation.class);

                if (checkAnnotation != null) {
                    permissions = checkAnnotation.permissions();
                    flags = checkAnnotation.flags();
                }
                processor.registerZeroArgCommand(new ZeroArgCommand(flags, permissions, (Invocation) clazz.getDeclaredConstructor().newInstance()));
            }
            for (ClassEntry<SubCommand> classEntry : classList) {
                SubCommand subCommandAnnotation = classEntry.getAnnotation();

                if (subCommandAnnotation.appliedCommand().equals(command)) {
                    Class<?> clazz = classEntry.getClazz();

                    if (!Invocation.class.isAssignableFrom(clazz))
                        throw new InvalidImplementationException(clazz, Invocation.class);

                    Check checkAnnotation = (Check) classEntry.getAdditionalAnnotations().stream().filter(a -> a.annotationType().equals(Check.class)).findFirst().orElse(null);

                    String[] permissions = new String[] { };
                    LunaSpringSubCommand.AccessFlag[] flags = new ZeroArgCommand.AccessFlag[] { };

                    if (checkAnnotation != null) {
                        permissions = checkAnnotation.permissions();
                        flags = checkAnnotation.flags();
                    }

                    Invocation commandInstance = (Invocation) clazz.getDeclaredConstructor().newInstance();

                    LunaSpringSubCommand subCommand = new LunaSpringSubCommand(
                            subCommandAnnotation.commandIdentifiers(),
                            flags,
                            permissions,
                            commandInstance);

                    if (LunaCompleter.class.isAssignableFrom(clazz)) {
                        LunaCompleter lunaCompleter = ((LunaCompleter) commandInstance);
                        subCommand.setTabCompleter(lunaCompleter);
                    }
                    processor.registerSubCommand(subCommand);
                    plugin.registerCommandProcessor(processor);
                }
            }
        }
    }
}
