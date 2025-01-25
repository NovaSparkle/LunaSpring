package org.novasparkle.lunaspring.Util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.managers.ColorManager;

import java.util.Random;
import java.util.logging.Logger;

@UtilityClass
public class Utils {
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
        Logger logger = Logger.getLogger("Minecraft");

        if (LunaSpring.getProvider().isRegistered(ColorManager.getColorService().getClass())) {
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
}