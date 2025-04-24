package org.novasparkle.lunaspring;

import lombok.SneakyThrows;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaPAPIExpansion;
import org.novasparkle.lunaspring.self.LSConfig;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class LunaPlugin extends JavaPlugin {
    private void startMessage(List<String> startMessage) {
        String textColorString = LSConfig.getString("on_load_plugin_text_colors");
        char endedColor = textColorString == null || textColorString.isEmpty() ? 'b' :
                textColorString.charAt(LunaMath.getRandomInt(0, textColorString.length()));
        String formattedEndedColor = "&" + endedColor;

        String authors = this.getAuthors();
        startMessage.forEach(m -> {
            String line = m
                    .replace("^", formattedEndedColor)
                    .replace("[pluginName]", this.getName())
                    .replace("[pluginVersion]", this.getVersion())
                    .replace("[pluginAuthors]", authors == null || authors.isEmpty() ? "NoData" : authors)
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
        if (new File(this.getDataFolder(), path).exists()) return;
        this.saveResource(path, false);
        this.info(LSConfig.getMessage("loadedSource").replace("[file]", path));
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
            this.info(LSConfig.getMessage("placeholderRegistered").replace("[identifier]", identifier));
        }
    }

    public void createPlaceholder(LunaPAPIExpansion.Request request) {
        this.createPlaceholder(null, request);
    }

    /**
     * Создание нового и копирование содержимого файла из ресурсов (папки resources в вашем проекте Bukkit) в новый файл targetFile
     * <p>
     * @param destinationPath - путь файла, который будет создан сразу с содержимым из файла по пути copyResourcePath
     * @param copyResourcePath - путь до файла, из которого будет браться код для копирования
     */
    @SneakyThrows
    public void copyFile(String destinationPath, String copyResourcePath) {
        Files.createDirectories(Paths.get(destinationPath).getParent());

        InputStream inputStream = this.getResource(copyResourcePath);
        try (OutputStream outputStream = new FileOutputStream(destinationPath)) {
            byte[] buffer = new byte[1024];
            int length;

            if (inputStream == null) return;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        inputStream.close();
    }


    @Override
    @OverridingMethodsMustInvokeSuper
    public void onEnable() {
        if (LunaSpring.getINSTANCE().getHookedPlugins().contains(this) || this.equals(LunaSpring.getINSTANCE())) return;

        this.startMessage(Arrays.asList(
                "",
                "        ^ | &l[pluginName]^ v[pluginVersion] (by [pluginAuthors])",
                "        ^ | &fEngined with ^&lLunaSpring^ v[LSVersion]",
                "        ^ | &fAuthor: ^NovaSparkle",
                "        ^ | &fDev-Helper: ^ProGiple",
                ""
        ));
        LunaSpring.getINSTANCE().hookPlugin(this);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
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
    }
}
