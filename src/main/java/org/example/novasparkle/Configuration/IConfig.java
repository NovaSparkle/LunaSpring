package org.example.novasparkle.Configuration;

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
    protected File file;
    public IConfig(Plugin plugin) {
        this.config = plugin.getConfig();
    }

    public IConfig(String fileName, String filePath) {
        this.file = new File(filePath + File.pathSeparator + fileName + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
    public IConfig(File container, String fileName) {
        this.file = new File(container, fileName + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
    protected void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
    protected void reload(Plugin plugin) {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
    protected String getString(String path) {
        return this.config.getString(path);
    }

    protected boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    protected List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    protected int getInt(String path) {
        return this.config.getInt(path);
    }

    protected ItemStack getItemStack(String path) {
        return this.config.getItemStack(path);
    }

    protected List<Integer> getIntList(String path) {
        return this.config.getIntegerList(path);
    }

    protected ConfigurationSection getSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    protected Material getMaterial(String path) {
        return Material.getMaterial(this.getString(path));
    }
}