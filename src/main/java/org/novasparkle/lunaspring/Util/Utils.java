package org.novasparkle.lunaspring.Util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.logging.Logger;

@UtilityClass
public class Utils {
    public int getIndex(int row, int col) {
        return (row - 1) * 9 + col - 1;
    }
    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    public static int toInt(String text) {
        return Integer.parseInt(text);
    }
    public static void info(String text) {
        Logger logger = Logger.getLogger("Minecraft");

        if (LunaSpring.getINSTANCE().getProvider().isRegistered(ColorManager.class)) {
            logger.info(ColorManager.color(text));
        } else logger.info(Utils.color(text));
    }
}