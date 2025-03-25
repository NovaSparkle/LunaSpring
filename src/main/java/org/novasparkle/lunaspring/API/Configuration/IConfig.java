package org.novasparkle.lunaspring.API.Configuration;

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
import org.novasparkle.lunaspring.API.Util.utilities.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IConfig {
    protected FileConfiguration config;
    @Getter
    private File file;
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

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

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

    @SuppressWarnings("deprecation")
    public void sendMessage(CommandSender sender, String id, String... replacements) {
        List<String> message = new ArrayList<>(config.getStringList(String.format("messages.%s", id)));
        if (message.isEmpty()) return;
        for (String line : message) {
            byte index = 0;
            for (String replacement : replacements) {
                line = line.replace("{" + index + "}", replacement);
                index++;
            }

            String newLine = Utils.color(line
                    .replace("ACTION_BAR ", "")
                    .replace("TITLE ", "")
                    .replace("SOUND ", ""));
            if (sender instanceof Player player &&
                    (line.startsWith("ACTION_BAR") || line.startsWith("SOUND") || line.startsWith("TITLE"))) {
                if (line.startsWith("ACTION_BAR")) player.sendActionBar(newLine);
                else if (line.startsWith("SOUND")) player.playSound(player.getLocation(), Sound.valueOf(newLine), 1, 1);
                else {
                    String[] split = newLine.split("\\{S}");
                    if (split.length < 2) split = new String[]{split[0], ""};
                    player.sendTitle(split[0], split[1], 15, 20, 15);
                }
            }
            else sender.sendMessage(newLine);
        }
    }
}