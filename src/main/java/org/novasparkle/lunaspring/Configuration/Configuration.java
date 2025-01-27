package org.novasparkle.lunaspring.Configuration;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configuration extends IConfig {

    public Configuration(String filePath) {
        super(filePath);
    }

    public Configuration(File container, String fileName) {
        super(container, fileName);
    }

    public Configuration(File container, File file) {
        super(container, file);
    }

    public Configuration(File file) {
        super(file);
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
    }

    public void setString(String path, String value) {
        this.config.set(path, value);
    }

    public void setInt(String path, int value) {
        this.config.set(path, value);
    }

    public void setBoolean(String path, boolean value) {
        this.config.set(path, value);
    }

    public void setStringList(String path, List<String> value) {
        this.config.set(path, value);
    }

    public void setIntList(String path, List<Integer> value) {
        this.config.set(path, value);
    }

    public void setMaterial(String path, Material value) {
        this.config.set(path, value.toString());
    }

    public void setItemStack(String path, ItemStack value) {
        this.config.set(path, value);
    }
    public ConfigurationSection createSection(ConfigurationSection section, String name) {
        return section.createSection(name);
    }
    public ConfigurationSection createSection(String path, String name) {
        return this.getSection(path).createSection(name);
    }

    public void save() {
        try {
            this.config.save(this.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
