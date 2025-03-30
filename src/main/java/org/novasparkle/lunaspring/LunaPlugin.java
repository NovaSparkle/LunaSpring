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
import org.novasparkle.lunaspring.API.Util.utilities.Utils;
import org.novasparkle.lunaspring.API.Util.Service.managers.ColorManager;
import org.novasparkle.lunaspring.API.Util.utilities.LunaPAPIExpansion;
import org.novasparkle.lunaspring.self.ConfigManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LunaPlugin extends JavaPlugin {

    /**
     * Обязательный метод для инициализации класса
    */
    public final void initialize() {
        this.startMessage(Arrays.asList(
                "",
                "        ^ | &l[pluginName]^ v[pluginVersion] (by [pluginAuthors])",
                "        ^ | &fEngined with ^&lLunaSpring^ v[LSVersion]",
                "        ^ | &fAuthor: ^NovaSparkle",
                "        ^ | &fDev-Helper: ^ProGiple",
                ""
        ));
        LunaSpring.getINSTANCE().hookPlugin(this);
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
                    .replace("[pluginAuthors]", this.getAuthors())
                    .replace("[LSVersion]", LunaSpring.getINSTANCE().getVersion());
            this.info(line);
        });
    }

    /**
     * Получить список авторов плагина
     */
    public String getAuthors() {
        return String.join(", ", this.getDescription().getAuthors());
    }

    /**
     * Получить версию плагина
     */
    public String getVersion() {
        return this.getDescription().getVersion();
    }

    /**
     * Загрузить файл из resources
     */
    public void loadFile(String path) {
        this.saveResource(path, false);
        this.info(ConfigManager.getMessage("loadedSource").replace("[file]", path));
    }

    /**
     * Загрузить все перечисленные файлы из resources
     */
    public void loadFiles(boolean enabled, String... paths) {
        if (!enabled) return;
        Arrays.stream(paths).forEach(this::loadFile);
    }

    /**
     * Зарегистрировать команду плагина
     */
    public void registerCommand(CommandExecutor command, String stringCommand) {
        Objects.requireNonNull(this.getCommand(stringCommand)).setExecutor(command);
    }

    /**
     * Зарегистрировать TabCompleter плагина
     */
    public void registerTabCompleter(TabCompleter tabCompleter, String stringCommand) {
        Objects.requireNonNull(this.getCommand(stringCommand)).setTabCompleter(tabCompleter);
    }

    /**
     * Зарегистрировать TabExecutor плагина
     */
    public void registerTabExecutor(TabExecutor tabExecutor, String stringCommand) {
        registerCommand(tabExecutor, stringCommand);
        registerTabCompleter(tabExecutor, stringCommand);
    }

    /**
     * Зарегистрировать слушатель плагина
     */
    public void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    /**
     * Вывод информации в консоль
     */
    public void info(String text) {
        this.getLogger().info(ColorManager.color(text));
    }

    /**
     * Вывод предупреждения в консоль
     */
    public void warning(String text) {
        this.getLogger().warning(ColorManager.color(text));
    }

    /**
     * Регистрация плейсхолдера
     */
    public void registerPlaceholder(PlaceholderExpansion placeholderExpansion) {
        if (Utils.isPluginEnabled("PlaceholderAPI")) placeholderExpansion.register();
    }

    /**
     * Создавние плейсхолдера.
     * @param request функциональный интерфейс, который обрабатывает метод
     * onRequest(OfflinePlayer player, String params)
     * <p>
     * Не требует зависимости PlaceholderAPI внутри pom.xml!
     */
    public void createPlaceholder(String identifier, LunaPAPIExpansion.Request request) {
        if (Utils.isPluginEnabled("PlaceholderAPI")) {
            new LunaPAPIExpansion(this, identifier, request).register();
            this.info(ConfigManager.getMessage("placeholderRegistered").replace("[identifier]", identifier));
        }
    }

    public void createPlaceholder(LunaPAPIExpansion.Request request) {
        this.createPlaceholder(null, request);
    }

    /**
     * Реализация логики выключения плагина по умолчанию
     */
    @Override
    public void onDisable() {
        if (this.equals(LunaSpring.getINSTANCE())) return;
        this.startMessage(Arrays.asList(
                "",
                "        ^ | &n[pluginName]^ v[pluginVersion] (by [pluginAuthors])",
                "        ^ | &fDisabling with ^&lLunaSpring^ v[LSVersion]",
                "        ^ | &fDeveloped by ^NovaSparkle",
                "        ^ | &fDev-Helper: ^ProGiple",
                "        ^ | ^GoodBye!",
                ""
        ));
        Bukkit.getScheduler().cancelTasks(this);
    }
}
