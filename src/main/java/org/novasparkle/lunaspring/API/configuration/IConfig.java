package org.novasparkle.lunaspring.API.configuration;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.AnnounceUtils;
import org.novasparkle.lunaspring.API.util.utilities.ComponentUtils;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.io.File;
import java.util.List;
import java.util.function.Function;

public class IConfig {
    protected FileConfiguration config;
    @Getter
    private File file;
    /**
     * Конструктор для config.yml
     */
    public IConfig(Plugin plugin) {
        this.config = plugin.getConfig();
    }

    public IConfig(String filePath) {
        this(new File(filePath));
    }

    public IConfig(File container, String fileName) {
        this(new File(container, fileName + ".yml"));
    }

    public IConfig(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    @Override
    public String toString() {
        return "IConfig{" +
                "config=" + config +
                ", file=" + file.getName() +
                '}';
    }

    /**
     * Перезагрузка кастомного конфига
     */
    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Перезагрузка config.yml
     */
    public void reload(Plugin plugin) {
        if (this.file == null || !this.file.exists()) plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public Object getObject(String path) {
        return this.config.get(path);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public Location getLocation(String path) {
        return this.config.getLocation(path);
    }

    /**
     * Получить инстанс Location из секции типа:<br>
     * location:<br>
     *   world: world<br>
     *   x: 0<br>
     *   y: 0<br>
     *   z: 0<br>
     */
    public Location getLocation(ConfigurationSection section) {
        String world = section.getString("world");
        assert world != null;
        return new Location(Bukkit.getWorld(world),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch"));
    }

    /**
     * Получение типов данных из конфигураций по указанному пути.
     */

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public ItemStack getItemStack(String path) {
        return this.config.getItemStack(path);
    }

    public List<Integer> getIntList(String path) {
        return this.config.getIntegerList(path);
    }

    public ConfigurationSection getSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public Material getMaterial(String path) {
        return Material.valueOf(this.getString(path));
    }

    public FileConfiguration self() {
        return this.config;
    }

    public String getMessage(String id, String... replacements) {
        return ColorManager.color(Utils.applyReplacements(this.getString(String.format("messages.%s", id)), replacements));
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public long getLong(String path) {
        return this.config.getLong(path);
    }

    public <T> T readFields(String path, Function<ConfigurationSection, T> function) {
        ConfigurationSection section = this.getSection(path);
        return section == null ? null : function.apply(section);
    }

    public <T> T deserialize(Class<T> tClass, String path) {
        return Utils.Base64.deserialize(tClass, this.getString(path));
    }

    /**
    * Отправка сообщения для sender с айди сообщения id из секции конфигурации messages (данные сообщения указаны в формате списка (List<String>)),
    * массив replacements нужен для указания локальных заменителей для сообщения, если указать сразу три заменителя (например никнейм игрока,
    * префикс его привилегии и его индивидуальный код), то в конфиге сообщения надо будет указать индексы этих заменителей в фигурных скобках (например,
    * {0} - ник игрока, {1} - префикс, а {2} - код). Если sender является игроком, то можно использовать отправку ACTION_BAR, TITLE и SOUND,
    * указанные в строках сообщения.
    * <h3><u>Пример:</u></h3>
    * messages:<br>
    *  example_message_id:<br>
    *    - "message!"<br>
    *    - "[ACTION_BAR] &bhello!"<br>
    *    - "[TITLE] &bMESSAGES <S> &nexample" // <S> (split) - разделитель TITLE и SUBTITLE<br>
    *    - "[SOUND] UI_BUTTON_CLICK"<br>
     */

    @SuppressWarnings("deprecation")
    public void sendMessage(String messagesPath, CommandSender sender, String id, String... replacements) {
        String path = String.format("%s.%s", messagesPath, id);

        List<String> message = Lists.newArrayList(this.config.getStringList(path));
        if (message.isEmpty()) {
            String stringMessage = this.config.getString(path);
            if (stringMessage != null && !stringMessage.isEmpty()) message.add(stringMessage);
        }

        for (String line : message) {
            line = Utils.applyReplacements(line, replacements);

            Player player = sender instanceof Player p ? p : null;
            if (player != null) line = Utils.setPlaceholders(player, line);

            String newLine = line
                    .replace("[ACTION_BAR] ", "")
                    .replace("[SOUND_ALL] ", "")
                    .replace("[BROADCAST] ", "")
                    .replace("[TITLE] ", "")
                    .replace("[TITLE_ALL] ", "")
                    .replace("[SUGGESTCOMMAND] ", "")
                    .replace("[RUNCOMMAND] ", "")
                    .replace("[URL] ", "")
                    .replace("[CLIPBOARDCOPY] ", "")
                    .replace("[SOUND] ", "")
                    .replace("[HOVER] ", "");

            if (line.startsWith("[SUGGESTCOMMAND]")) {
                sender.spigot().sendMessage(ComponentUtils.createClickableText(newLine, ClickEvent.Action.SUGGEST_COMMAND));
                continue;
            } else if (line.startsWith("[RUNCOMMAND]")) {
                sender.spigot().sendMessage(ComponentUtils.createClickableText(newLine, ClickEvent.Action.RUN_COMMAND));
                continue;
            } else if (line.startsWith("[HOVER]")) {
                sender.spigot().sendMessage(ComponentUtils.createHoverableText(newLine));
                continue;
            } else if (line.startsWith("[URL]")) {
                sender.spigot().sendMessage(ComponentUtils.createClickableText(newLine, ClickEvent.Action.OPEN_URL));
                continue;
            } else if (line.startsWith("[CLIPBOARDCOPY]")) {
                sender.spigot().sendMessage(ComponentUtils.createClickableText(newLine, ClickEvent.Action.COPY_TO_CLIPBOARD));
                continue;
            }

            newLine = ColorManager.color(newLine);
            if (line.startsWith("[ACTION_BAR]")) {
                if (player == null) continue;
                player.sendActionBar(newLine);
            }
            else if (line.startsWith("[SOUND]")) {
                if (player == null) continue;
                AnnounceUtils.sound(player, newLine);
            }
            else if (line.startsWith("[SOUND_ALL]")) {
                AnnounceUtils.soundAll(newLine);
            }
            else if (line.startsWith("[TITLE]")) {
                if (player == null) continue;
                AnnounceUtils.title(player, newLine);
            }
            else if (line.startsWith("[TITLE_ALL]")) {
                AnnounceUtils.titleAll(newLine);
            }
            else if (line.startsWith("[BROADCAST]")) {
                AnnounceUtils.broadcast(newLine);
            }
            else sender.sendMessage(newLine);
        }
    }

    public void sendMessage(CommandSender sender, String id, String... replacements) {
        this.sendMessage("messages", sender, id, replacements);
    }
}