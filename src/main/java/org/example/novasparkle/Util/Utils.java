package org.example.novasparkle.Util;

import org.bukkit.ChatColor;

public class Utils {
    public static int getIndex(int row, int col) {
        return (row - 1) * 9 + col - 1;
    }
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    public static int toInt(String text) {
        return Integer.parseInt(text);
    }
}