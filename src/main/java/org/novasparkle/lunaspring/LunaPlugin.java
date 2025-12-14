package org.novasparkle.lunaspring;

import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.API.commands.processor.CommandProcessor;
import org.novasparkle.lunaspring.API.commands.annotations.LunaCommand;
import org.novasparkle.lunaspring.API.events.LunaHandler;
import org.novasparkle.lunaspring.API.util.exceptions.InvalidImplementationException;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.LunaPAPIExpansion;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.API.util.utilities.reflection.AnnotationScanner;
import org.novasparkle.lunaspring.API.util.utilities.reflection.ClassEntry;
import org.novasparkle.lunaspring.self.LSConfig;
import org.novasparkle.lunaspring.self.PaidPlugin;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public abstract class LunaPlugin extends JavaPlugin {
    @Accessors(fluent = true)
    private final List<CommandProcessor> processors = new ArrayList<>();

    private void startMessage(List<String> startMessage) {
        String textColorString = LSConfig.getString("on_load_plugin_text_colors");
        char endedColor = textColorString == null || textColorString.isEmpty() ? ' ' : textColorString.charAt(LunaMath.getRandomInt(0, textColorString.length()));
        String formattedEndedColor = endedColor == ' ' ? "" : "&" + endedColor;

        String authors = this.getAuthors();
        startMessage.forEach(m -> {
            String line = m
                    .replace("[color]", formattedEndedColor)
                    .replace("[pluginName]", this.getName())
                    .replace("[pluginVersion]", this.getVersion())
                    .replace("[pluginAuthors]", authors == null || authors.isEmpty() ? "" : String.format("(by %s)", authors))
                    .replace("[LSVersion]", LunaSpring.getInstance().getVersion());
            this.print(line);
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
     * Получить Jar файл плагина
     */
    public File getJar() {
        return this.getFile();
    }
    /**
     * Загрузить файл из resources
     */
    public boolean loadFile(String path) {
        if (new File(this.getDataFolder(), path).exists()) return false;
        this.saveResource(path, false);
        this.info(LSConfig.getMessage("loadedSource").replace("[file]", path));
        return true;
    }

    /**
     * Загрузить все перечисленные файлы из resources
     */
    public boolean loadFiles(String... paths) {
        boolean r = true;
        for (String s : paths)
            if (!this.loadFile(s)) r = false;
        return r;
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

    public void registerCommandProcessor(CommandProcessor processor) {
        this.registerTabExecutor(processor, processor.appliedCommand());
        if (!this.processors.contains(processor)) this.processors.add(processor);
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

    public void print(String text) {
        Utils.print(this, text);
    }

    /**
     * Регистрация плейсхолдера
     */
    public boolean registerPlaceholder(PlaceholderExpansion placeholderExpansion) {
        if (Utils.isPluginEnabled("PlaceholderAPI")) return placeholderExpansion.register();
        return false;
    }

    /**
     * Создавние плейсхолдера.
     * @param request функциональный интерфейс, который обрабатывает метод
     * onRequest(OfflinePlayer player, String params)
     * <p>
     * Не требует зависимости PlaceholderAPI внутри pom.xml!
     */
    public boolean createPlaceholder(String identifier, LunaPAPIExpansion.Request request) {
        if (Utils.isPluginEnabled("PlaceholderAPI")) {
            if (identifier == null || identifier.isEmpty()) identifier = this.getName().toLowerCase();
            boolean registered = new LunaPAPIExpansion(this, identifier, request).register();

            if (registered) this.info(LSConfig.getMessage("placeholderRegistered").replace("[identifier]", identifier));
            return registered;
        }
        return false;
    }

    public boolean createPlaceholder(LunaPAPIExpansion.Request request) {
        return this.createPlaceholder(null, request);
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
        if (this.equals(LunaSpring.getInstance())) {
            this.startMessage(Arrays.asList(
                    "",
                    "       [color]╭╮╱╱╱╱╱╱╱╱╱╱╱╭━━━╮ v[LSVersion]",
                    "       [color]┃┃╱╱╭╮╭┳━╮╭━━┫╰━━┳━━┳━┳┳━╮╭━━╮",
                    "       [color]┃┃╱╭┫┃┃┃╭╮┫╭╮┣━━╮┃╭╮┃╭╋┫╭╮┫╭╮┃",
                    "       [color]┃╰━╯┃╰╯┃┃┃┃╭╮┃╰━╯┃╰╯┃┃┃┃┃┃┃╰╯┃",
                    "       [color]╰━━━┻━━┻╯╰┻╯╰┻━━━┫╭━┻╯╰┻╯╰┻━╮┃",
                    "       [color]╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱┃┃╱╱╱╱╱╱╱╭━╯┃",
                    "       [color]                 ╰╯       ╰━━╯",
                    "       [color]| &fAuthor: [color]NovaSparkle",
                    "       [color]| &fDev-Helper: [color]ProGiple",
                    ""
            ));
            return;
        }

        if (LunaSpring.getInstance().getHookedPlugins().contains(this)) return;
        this.startMessage(Arrays.asList(
                "",
                "        [color] | &l[pluginName][color] v[pluginVersion] [pluginAuthors]",
                "        [color] | &fEngined with [color]&lLunaSpring[color] v[LSVersion]",
                "        [color] | &fAuthor: [color]NovaSparkle",
                "        [color] | &fDev-Helper: [color]ProGiple",
                ""
        ));

        LunaSpring.getInstance().hookPlugin(this);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        if (this.equals(LunaSpring.getInstance())) {
            this.startMessage(Arrays.asList(
                    "",
                    "       [color]╭╮╱╱╱╱╱╱╱╱╱╱╱╭━━━╮ v[LSVersion]",
                    "       [color]┃┃╱╱╭╮╭┳━╮╭━━┫╰━━┳━━┳━┳┳━╮╭━━╮",
                    "       [color]┃┃╱╭┫┃┃┃╭╮┫╭╮┣━━╮┃╭╮┃╭╋┫╭╮┫╭╮┃",
                    "       [color]┃╰━╯┃╰╯┃┃┃┃╭╮┃╰━╯┃╰╯┃┃┃┃┃┃┃╰╯┃",
                    "       [color]╰━━━┻━━┻╯╰┻╯╰┻━━━┫╭━┻╯╰┻╯╰┻━╮┃",
                    "       [color]╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱┃┃╱╱╱╱╱╱╱╭━╯┃",
                    "       [color]                 ╰╯       ╰━━╯",
                    "       [color]| &fAuthor: [color]NovaSparkle",
                    "       [color]| &fDev-Helper: [color]ProGiple",
                    "       [color]| Plugin disabling... Goodbye!",
                    ""
            ));
            return;
        }

        this.startMessage(Arrays.asList(
                "",
                "        [color] | &n[pluginName][color] v[pluginVersion] [pluginAuthors]",
                "        [color] | &fDisabling with [color]&lLunaSpring[color] v[LSVersion]",
                "        [color] | &fDeveloped by [color]NovaSparkle",
                "        [color] | &fDev-Helper: [color]ProGiple",
                "        [color] | [color]GoodBye!",
                ""
        ));
    }
    @SneakyThrows
    public void processCommands(String... allowedPackages) {
        Set<ClassEntry<LunaCommand>> classes = AnnotationScanner.findAnnotatedClasses(this, LunaCommand.class, allowedPackages);
        for (ClassEntry<LunaCommand> entry : classes) {
            Class<?> clazz = entry.getClazz();

            String command = clazz.getDeclaredAnnotation(LunaCommand.class).value();
            if (TabExecutor.class.isAssignableFrom(clazz)) {
                TabExecutor tabExecutor = (TabExecutor) clazz.getDeclaredConstructor().newInstance();
                this.registerTabExecutor(tabExecutor, command);

            } else if (CommandExecutor.class.isAssignableFrom(clazz)) {
                CommandExecutor tabExecutor = (CommandExecutor) clazz.getDeclaredConstructor().newInstance();
                this.registerCommand(tabExecutor, command);
            } else throw new InvalidImplementationException(clazz, CommandExecutor.class);
        }
    }

    @SneakyThrows
    public void processListeners(String... allowedPackages) {
        Set<ClassEntry<LunaHandler>> classes = AnnotationScanner.findAnnotatedClasses(this, LunaHandler.class, allowedPackages);
        for (ClassEntry<LunaHandler> entry : classes) {
            Class<?> clazz = entry.getClazz();
            if (Listener.class.isAssignableFrom(entry.getClazz())) {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                this.registerListeners(listener);
            } else throw new InvalidImplementationException(clazz, Listener.class);
        }
    }

    public boolean isPaid() {
        return this.getClass().isAnnotationPresent(PaidPlugin.class);
    }

    public CommandProcessor getProcessor(String appliedCommand) {
        return Utils.find(this.processors, p -> p.appliedCommand().equalsIgnoreCase(appliedCommand)).orElse(null);
    }
}
