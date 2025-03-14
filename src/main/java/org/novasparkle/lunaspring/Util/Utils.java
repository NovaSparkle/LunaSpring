package org.novasparkle.lunaspring.Util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.realized.RegionService;
import org.novasparkle.lunaspring.Util.managers.ColorManager;
import org.novasparkle.lunaspring.Util.managers.RegionManager;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@UtilityClass
public class Utils {
    public final Random random = new Random();

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

    public Location findRandomLocation(World world, int maxX, int maxY, int minY, int maxZ, List<String> invalidBlocks) {
        if (world == null) return null;

        int x = random.nextInt(maxX * 2) - maxX;
        int z = random.nextInt(maxZ * 2) - maxZ;
        int y = maxY;

        Location location = new Location(world, x, y, z);
        while (!invalidBlocks.contains(location.getBlock().getType().name()) && y > minY) {
            location.setY(--y);
        }
        location.setY(++y);
        return location;
    }

    public Location findRandomLocation(World world, int maxX, int maxY, int maxZ, List<String> invalidBlocks) {
        return findRandomLocation(world, maxX, maxY, 0, maxZ, invalidBlocks);
    }

    public Location findRandomLocation(World world, int maxY, int minY, List<String> invalidBlocks) {
        int border = (int) (world.getWorldBorder().getSize() / 2);
        return findRandomLocation(world, border, maxY, minY, border, invalidBlocks);
    }

    public Location findRandomLocation(World world, int maxY, List<String> invalidBlocks) {
        return findRandomLocation(world, maxY, 0, invalidBlocks);
    }

    public Location findRandomLocation(World world, List<String> invalidBlocks) {
        return findRandomLocation(world, 256, 0, invalidBlocks);
    }
}