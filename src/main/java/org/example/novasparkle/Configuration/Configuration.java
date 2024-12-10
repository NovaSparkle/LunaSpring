package org.example.novasparkle.Configuration;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;

public class Configuration extends IConfig {

    public Configuration(String fileName, String filePath) {
        super(fileName, filePath);
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


    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
