package org.novasparkle.lunaspring.Util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.realized.RegionService;
import org.novasparkle.lunaspring.Util.managers.ColorManager;
import org.novasparkle.lunaspring.Util.managers.RegionManager;
import org.stringtemplate.v4.ST;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;

@UtilityClass
public class Utils {
    @Getter
    private final Random random = new Random();

    public int getIndex(int row, int col) {
        return (row - 1) * 9 + col - 1;
    }

    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public int toInt(String text) {
        return Integer.parseInt(text);
    }

    public void info(String text) {
        Logger logger = LunaSpring.getPlugin().getLogger();

        if (LunaSpring.getServiceProvider().isRegistered(ColorManager.getColorService().getClass())) {
            logger.info(ColorManager.color(text));
        } else logger.info(Utils.color(text));
    }

    public String getRKey(byte size) {
        String kit = "qwertyuiopasdfghjklzxcvbnm1234567890";
        return Utils.getRKey(size, kit);
    }

    public String getRKey(byte size, String kit) {
        StringBuilder endValue = new StringBuilder();
        byte kitSize = (byte) kit.toCharArray().length;

        Random random = new Random();
        for (byte i = 0; i < size; i++) {
            endValue.append(kit.charAt(random.nextInt(kitSize)));
        }
        return endValue.toString();
    }

    public Location findRandomLocation(World world, int maxX, int maxZ) {
        if (world == null) return null;
        if (maxX == 0 || maxZ == 0) return findRandomLocation(world);

        int x = random.nextInt(maxX * 2) - maxX;
        int z = random.nextInt(maxZ * 2) - maxZ;
        int y = world.getHighestBlockYAt(x, z);

        return new Location(world, x, y, z);
    }

    public Location findRandomLocation(World world) {
        int border = (int) (world.getWorldBorder().getSize() / 2);
        if (border == 0) return null;

        return findRandomLocation(world, border, border);
    }

    public void registerListener(JavaPlugin plugin, Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void reg(JavaPlugin plugin, Listener listener) {
        registerListener(plugin, listener);
    }

    public boolean hasPlugin(String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }

    public LocalTime getNextTime(Collection<LocalTime> times) {
        LocalTime currentTime = LocalTime.now();
        for (LocalTime time : times) {
            if (time.isAfter(currentTime)) return time;
        }
        return null;
    }
    public boolean isPluginEnabled(String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }
    public void registerCommand(CommandExecutor command, String stringCommand) {
        Objects.requireNonNull(LunaSpring.getPlugin().getCommand(stringCommand)).setExecutor(command);
    }
    public void registerTabCompleter(TabCompleter tabCompleter, String stringCommand) {
        Objects.requireNonNull(LunaSpring.getPlugin().getCommand(stringCommand)).setTabCompleter(tabCompleter);
    }
    public void registerTabExecutor(TabExecutor tabExecutor, String stringCommand) {
        registerCommand(tabExecutor, stringCommand);
        registerTabCompleter(tabExecutor, stringCommand);
    }
}