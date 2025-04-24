package org.novasparkle.lunaspring.API.configuration;

import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;


public class Configuration extends IConfig {

    public Configuration(String filePath) {
        super(filePath);
    }

    public Configuration(File container, String fileName) {
        super(container, fileName);
    }

    public Configuration(File file) {
        super(file);
    }

    /**
     * Установка произвольного значения по указанному пути.
     */
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

    public void setSection(String path, ConfigurationSection value) {
        this.config.set(path, value);
    }

    /**
     * Запись локации.
     * @param asLocation если true, то локация запишется в одну строку, если false, то запишется в виде секции со всеми данными.
     * @param asBlock отвечает за сами данные, если стоит true - будут записаны только мир и координаты формата integer, а если поставить на false, то
     *      будут записаны мир, координаты в формате double и значения угла обзора (Yaw + Pitch).
     */
    public void setLocation(String path, Location value, boolean asLocation, boolean asBlock) {
        if (asLocation)
            this.config.set(path, value);
        else {
            this.config.set(String.format("%s.world", path), value.getWorld().getName());
            if (asBlock) {
                this.config.set(String.format("%s.x", path), value.getBlockX());
                this.config.set(String.format("%s.y", path), value.getBlockY());
                this.config.set(String.format("%s.z", path), value.getBlockZ());
            } else {
                this.config.set(String.format("%s.x", path), value.getX());
                this.config.set(String.format("%s.y", path), value.getY());
                this.config.set(String.format("%s.z", path), value.getZ());
                this.config.set(String.format("%s.yaw", path), value.getYaw());
                this.config.set(String.format("%s.pitch", path), value.getPitch());
            }
        }
    }

    /**
     * Создать новую секцию.
     * @param section секция родитель.
     * @param name название новой секции.
     */
    public ConfigurationSection createSection(ConfigurationSection section, String name) {
        return section.createSection(name);
    }

    /**
     * Создать новую секцию.
     * @param path путь до секции родителя.
     * @param name название новой секции.
     */
    public ConfigurationSection createSection(String path, String name) {
        if (path == null)
            return this.config.createSection(name);
        return this.config.createSection(String.format("%s.%s", path, name));
    }

    /**
     * Сохранить конфигурацию.
     */
    @SneakyThrows
    public void save() {
        this.config.save(this.getFile());
    }
}
