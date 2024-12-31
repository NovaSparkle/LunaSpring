package org.example.novasparkle.Util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

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
}