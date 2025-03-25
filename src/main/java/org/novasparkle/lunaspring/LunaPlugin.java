package org.novasparkle.lunaspring;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.API.Events.MenuHandler;
import org.novasparkle.lunaspring.API.Util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.Util.Service.realized.ColorService;
import org.novasparkle.lunaspring.API.Util.utilities.Utils;
import org.novasparkle.lunaspring.API.Util.managers.ColorManager;
import org.novasparkle.lunaspring.API.Util.utilities.LunaPAPIExpansion;
import org.novasparkle.lunaspring.self.ConfigManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class LunaPlugin extends JavaPlugin {
    public final void initialize() {
        this.startMessage(Arrays.asList(
                "",
                "        ^ | &l[pluginName]^ v[pluginVersion]",
                "        ^ | &fEngined with ^&lLunaSpring^ v[LSVersion]",
                "        ^ | &fAuthor: ^NovaSparkle",
                "        ^ | &fDev-Helper: ^ProGiple",
                ""
        ));
        this.registerListener(new MenuHandler());
        this.info(ConfigManager.getMessage("listenerRegistered"));
    }
    private void startMessage(List<String> startMessage) {
        String textColorString = ConfigManager.getString("on_load_plugin_text_colors");
        char endedColor = textColorString == null || textColorString.isEmpty() ? 'b' :
                textColorString.charAt(LunaMath.getRandomInt(0, textColorString.length()));
        String formattedEndedColor = "&" + endedColor;

        startMessage.forEach(m -> {
            String line = m
                    .replace("^", formattedEndedColor)
                    .replace("[pluginName]", this.getName())
                    .replace("[pluginVersion]", this.getVersion())
                    .replace("[LSVersion]", LunaSpring.getINSTANCE().getVersion());
            this.info(line);
        });
    }
    public String getAuthors() {
        return String.join(", ", this.getDescription().getAuthors());
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }

    public void loadFile(String path) {
        this.saveResource(path, false);
        this.info(ConfigManager.getMessage("loadedSource").replace("[file]", path));
    }

    public void loadFiles(boolean enabled, String... paths) {
        if (!enabled) return;
        for (String path : paths) this.loadFile(path);
    }

    public void registerCommand(CommandExecutor command, String stringCommand) {
        Objects.requireNonNull(this.getCommand(stringCommand)).setExecutor(command);
    }

    public void registerTabCompleter(TabCompleter tabCompleter, String stringCommand) {
        Objects.requireNonNull(this.getCommand(stringCommand)).setTabCompleter(tabCompleter);
    }

    public void registerTabExecutor(TabExecutor tabExecutor, String stringCommand) {
        registerCommand(tabExecutor, stringCommand);
        registerTabCompleter(tabExecutor, stringCommand);
    }

    public void registerListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public void info(String text) {
        Logger logger = this.getLogger();

        ColorService colorService = ColorManager.getColorService();
        if (colorService != null && LunaSpring.getServiceProvider().isRegistered(colorService.getClass())) {
            logger.info(ColorManager.color(text));
        } else logger.info(Utils.color(text));
    }

    public void warning(String text) {
        Logger logger = this.getLogger();

        ColorService colorService = ColorManager.getColorService();
        if (colorService != null && LunaSpring.getServiceProvider().isRegistered(colorService.getClass())) {
            logger.warning(ColorManager.color(text));
        } else logger.warning(Utils.color(text));
    }

    public void registerPlaceholder(PlaceholderExpansion placeholderExpansion) {
        if (Utils.isPluginEnabled("PlaceholderAPI")) placeholderExpansion.register();
    }

    public void createPlaceholder(String identifier, LunaPAPIExpansion.Request request) {
        if (Utils.isPluginEnabled("PlaceholderAPI")) {
            new LunaPAPIExpansion(this, identifier, request).register();
            this.info(ConfigManager.getMessage("placeholderRegistered").replace("[identifier]", identifier));
        }
    }

    public void createPlaceholder(LunaPAPIExpansion.Request request) {
        this.createPlaceholder(null, request);
    }

    @Override
    public void onDisable() {
        this.startMessage(Arrays.asList(
                "",
                "        ^ | &n[pluginName]^ v[pluginVersion]",
                "        ^ | &fDisabling with ^&lLunaSpring^ v[LSVersion]",
                "        ^ | &fDeveloped by ^NovaSparkle",
                "        ^ | &fDev-Helper: ^ProGiple",
                "        ^ | ^GoodBye!",
                ""
        ));
        Bukkit.getScheduler().cancelTasks(this);
    }
}
