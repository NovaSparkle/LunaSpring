package org.novasparkle.lunaspring.API.util.service.managers.worldguard;

import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.LunaSpring;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;


public class ConfigManager {
    public static Configuration configuration;

    static {
        configuration = new Configuration(LunaSpring.getInstance().getDataFolder().getParentFile(), "LunaSpring/menu");
    }

    public static void set(String path, Object value) {
        configuration.set(path, value);
    }

    public static File getFile() {
        return configuration.getFile();
    }

    public static void setMaterial(String path, Material value) {
        configuration.setMaterial(path, value);
    }

    public static ConfigurationSection createSection(String path, String name) {
        return configuration.createSection(path, name);
    }

    @SneakyThrows
    public static void writeFields(String path, Object object) {
        configuration.writeFields(path, object);
    }

    public static ConfigurationSection createSection(ConfigurationSection section, String name) {
        return configuration.createSection(section, name);
    }

    @SneakyThrows
    public static void save() {
        configuration.save();
    }

    public static void setString(String path, String value) {
        configuration.setString(path, value);
    }

    public static void setInt(String path, int value) {
        configuration.setInt(path, value);
    }

    public static void setBoolean(String path, boolean value) {
        configuration.setBoolean(path, value);
    }

    public static void setStringList(String path, List<String> value) {
        configuration.setStringList(path, value);
    }

    public static void setIntList(String path, List<Integer> value) {
        configuration.setIntList(path, value);
    }

    public static void setItemStack(String path, ItemStack value) {
        configuration.setItemStack(path, value);
    }

    public static void setSection(String path, ConfigurationSection value) {
        configuration.setSection(path, value);
    }

    public static void setLocation(String path, Location value, boolean asLocation, boolean asBlock) {
        configuration.setLocation(path, value, asLocation, asBlock);
    }

    public static void serialize(String path, Serializable serializable) {
        configuration.serialize(path, serializable);
    }

    public static void setLong(String path, long value) {
        configuration.setLong(path, value);
    }

    public static void setDouble(String path, double value) {
        configuration.setDouble(path, value);
    }

    public static Object getObject(String path) {
        return configuration.getObject(path);
    }

    public static void reload() {
        configuration.reload();
    }

    public static void reload(Plugin plugin) {
        configuration.reload(plugin);
    }

    public static ItemStack getItemStack(String path) {
        return configuration.getItemStack(path);
    }

    public static <T> T deserialize(Class<T> tClass, String path) {
        return configuration.deserialize(tClass, path);
    }

    public static String getString(String path) {
        return configuration.getString(path);
    }

    public static Material getMaterial(String path) {
        return configuration.getMaterial(path);
    }

    public static boolean getBoolean(String path) {
        return configuration.getBoolean(path);
    }

    public static double getDouble(String path) {
        return configuration.getDouble(path);
    }

    public static ConfigurationSection getSection(String path) {
        return configuration.getSection(path);
    }

    public static String getMessage(String id, String... replacements) {
        return configuration.getMessage(id, replacements);
    }

    public static List<String> getStringList(String path) {
        return configuration.getStringList(path);
    }

    public static FileConfiguration self() {
        return configuration.self();
    }

    public static void sendMessage(CommandSender sender, String id, String... replacements) {
        configuration.sendMessage(sender, id, replacements);
    }

    public static List<Integer> getIntList(String path) {
        return configuration.getIntList(path);
    }

    public static void sendMessage(String messagesPath, CommandSender sender, String id, String... replacements) {
        configuration.sendMessage(messagesPath, sender, id, replacements);
    }

    public static <T> T readFields(String path, Function<ConfigurationSection, T> function) {
        return configuration.readFields(path, function);
    }

    public static Location getLocation(String path) {
        return configuration.getLocation(path);
    }

    public static Location getLocation(ConfigurationSection section) {
        return configuration.getLocation(section);
    }

    public static long getLong(String path) {
        return configuration.getLong(path);
    }

    public static int getInt(String path) {
        return configuration.getInt(path);
    }
}
