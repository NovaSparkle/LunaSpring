package org.novasparkle.lunaspring.other;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.Events.MenuHandler;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.LunaMath;
import org.novasparkle.lunaspring.Util.Service.realized.ColorService;
import org.novasparkle.lunaspring.Util.Utils;
import org.novasparkle.lunaspring.Util.managers.ColorManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class LunaPlugin extends JavaPlugin {
    public final void initialize() {
        String textColorString = LunaSpring.getINSTANCE().getConfig().getString("on_load_plugin_text_colors");
        char endedColor = textColorString == null || textColorString.isEmpty() ? 'b' :
                textColorString.charAt(LunaMath.getRandomInt(0, textColorString.length()));
        String formattedEndedColor = "&" + endedColor;

        List<String> startMessage = Arrays.asList(
                "",
                "        ^ | &l[pluginName]^ v[pluginVersion]",
                "        ^ | &fEngined with ^&nLunaSpring^ v[LSVersion]",
                "        ^ | &fAuthors: ^Nova Sparkle, ProGiple",
                ""
        );
        startMessage.forEach(m -> {
            String line = m
                    .replace("^", formattedEndedColor)
                    .replace("[pluginName]", this.getName())
                    .replace("[pluginVersion]", this.getVersion())
                    .replace("[LSVersion]", LunaSpring.getINSTANCE().getVersion());
            this.info(line);
        });
        this.registerListener(new MenuHandler());
    }

    public String getAuthors() {
        return String.join(", ", this.getDescription().getAuthors());
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }

    public void loadFile(String path) {
        this.saveResource(path, false);
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
        if (Utils.isPluginEnabled("PlaceholderAPI"))
            new LunaPAPIExpansion(this, identifier, request).register();
    }

    public void createPlaceholder(LunaPAPIExpansion.Request request) {
        this.createPlaceholder(null, request);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }
}
