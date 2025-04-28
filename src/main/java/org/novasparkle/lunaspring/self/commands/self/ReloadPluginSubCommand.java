package org.novasparkle.lunaspring.self.commands.self;

import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.AppliedCommand;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

//@SubCommand(maxArgs = 2, commandIdentifiers = {"pluginreload", "plr"}, flags = {})
@AppliedCommand("lunaspring")
public class ReloadPluginSubCommand extends LunaSpringSubCommand {
    private final PluginManager pluginManager;

    public ReloadPluginSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
        this.pluginManager = LunaSpring.getINSTANCE().getServer().getPluginManager();
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (!checkCommand(sender, args, "rlp")) return;
        this.unloadPlugin(args[1]);
        this.loadPlugin(args[1]);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void unloadPlugin(String pluginName) {
        SimplePluginManager spmanager = (SimplePluginManager) this.pluginManager;

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
            knownCommands = commandMap.getKnownCommands();
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
    }

    @SneakyThrows
    private void loadPlugin(String pluginName) {
        if (Utils.hasPlugin(pluginName)) {
            Plugin plugin = this.pluginManager.getPlugin(pluginName);
            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File file = (File) getFileMethod.invoke(plugin);
            Plugin loadedPlugin = this.pluginManager.loadPlugin(file);
            assert loadedPlugin != null;
            loadedPlugin.onLoad();
            pluginManager.enablePlugin(loadedPlugin);
            loadedPlugin.onEnable();
        }
    }
}
