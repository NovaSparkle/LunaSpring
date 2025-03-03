package org.novasparkle.lunaspring.Configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class IConfig {
    protected FileConfiguration config;
    @Getter
    private File file;
    public IConfig(Plugin plugin) {
        this.config = plugin.getConfig();
    }

    public IConfig(String filePath) {
        this.file = new File(filePath);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
    public IConfig(File container, String fileName) {
        this.file = new File(container, fileName + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    @Override
    public String toString() {
        return "IConfig{" +
                "config=" + config +
                ", file=" + file.getName() +
                '}';
    }
    public IConfig(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(this.file);
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
        return new Location(Bukkit.getWorld(world), section.getInt("x"),section.getInt("y"),section.getInt("z"));
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
}