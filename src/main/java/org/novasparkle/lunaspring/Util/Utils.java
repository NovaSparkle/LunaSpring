package org.novasparkle.lunaspring.Util;

import lombok.Getter;
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

        int x = random.nextInt(maxX * 2) - maxX;
        int z = random.nextInt(maxZ * 2) - maxZ;
        int y = world.getHighestBlockYAt(x, z);

        return new Location(world, x, y, z);
    }

    public Location findRandomLocation(World world) {
        int border = (int) (world.getWorldBorder().getSize() / 2);
        return findRandomLocation(world, border, border);
    }
}