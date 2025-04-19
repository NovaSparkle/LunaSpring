package org.novasparkle.lunaspring.self.commands;

import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.novasparkle.lunaspring.API.Commands.LunaCommand;
import org.novasparkle.lunaspring.API.Commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.LunaSpring;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@LunaCommand(maxArgs = 2, noConsole = false, commandIdentifiers = {"pluginreload", "plr"})
public class ReloadPluginSubCommand extends LunaSpringSubCommand {

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public boolean invoke() {
        String pluginName = this.getArgs()[2];
        SimplePluginManager spmanager = (SimplePluginManager) LunaSpring.getINSTANCE().getServer().getPluginManager();;

        // Список нативных команд
        Field pluginsField = spmanager.getClass().getDeclaredField("plugins");
        pluginsField.setAccessible(true);
        List<Plugin> plugins = (List<Plugin>) pluginsField.get(spmanager);

        // Мапа имен
        Field lookupNamesField = spmanager.getClass().getDeclaredField("lookupNames");
        lookupNamesField.setAccessible(true);
        Map<String, Plugin> lookupNames = (Map<String, Plugin>) lookupNamesField.get(spmanager);

        // Мапа команд
        Field commandMapField = spmanager.getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(spmanager);


        Map<String, Command> knownCommands = null;
        if (commandMap != null) {
            Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
        }

        Plugin[] pluginsList = spmanager.getPlugins();

        for (Plugin plugin : pluginsList) {
            if (plugin.getDescription().getName().equals(pluginName)) {
                spmanager.disablePlugin(plugin);
                plugins.remove(plugin);
                lookupNames.remove(pluginName);
                if (commandMap != null) {
                    for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
                        if (entry.getValue() instanceof PluginCommand pluginCommand && pluginCommand.getPlugin().equals(plugin)) {
                            pluginCommand.unregister(commandMap);
                        }
                    }
                }
            }
        }
        return false;
    }
}
