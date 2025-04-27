package org.novasparkle.lunaspring.API.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        return Material.getMaterial(this.getString(path));
    }

    public FileConfiguration self() {
        return this.config;
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
    *    - "ACTION_BAR &bhello!"<br>
    *    - "TITLE &bMESSAGES {S} &nexample" // {S} (split) - разделитель TITLE и SUBTITLE<br>
    *    - "SOUND UI_BUTTON_CLICK"<br>
     */
    @SuppressWarnings("deprecation")
    public void sendMessage(CommandSender sender, String id, String... replacements) {
        String path = String.format("messages.%s", id);

        String stringMessage = config.getString(path);
        if (stringMessage != null && !stringMessage.isEmpty() && !stringMessage.equalsIgnoreCase("[]")) {
            sender.sendMessage(ColorManager.color(Utils.applyReplacements(stringMessage)));
            return;
        }

        List<String> message = new ArrayList<>(config.getStringList(path));
        if (message.isEmpty()) return;
        for (String line : message) {
            line = Utils.applyReplacements(line, replacements);

            String newLine = ColorManager.color(line
                    .replace("ACTION_BAR ", "")
                    .replace("BROADCAST ", "")
                    .replace("TITLE ", "")
                    .replace("SOUND ", ""));
            if (line.startsWith("ACTION_BAR")) {
                if (!(sender instanceof Player player)) continue;
                player.sendActionBar(newLine);
            }
            else if (line.startsWith("SOUND")) {
                if (!(sender instanceof Player player)) continue;
                player.playSound(player.getLocation(), Sound.valueOf(newLine), 1, 1);
            }
            else if (line.startsWith("TITLE")) {
                if (!(sender instanceof Player player)) continue;

                String[] split = newLine.split("\\{S}");
                if (split.length < 2) split = new String[]{split[0], ""};
                player.sendTitle(split[0], split[1], 15, 20, 15);
            }
            else if (line.startsWith("BROADCAST")) {
                Utils.playersAction(p -> p.sendMessage(newLine));
            }
            else sender.sendMessage(newLine);
        }
    }
}