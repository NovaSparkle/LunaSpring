package org.novasparkle.lunaspring.Util;

import org.bukkit.configuration.file.FileConfiguration;
import org.novasparkle.lunaspring.Util.Service.ColorService;

public class ColorManager {
    protected static ColorService colorService;
    public static void init(FileConfiguration configuration) {
        colorService = new ColorService(configuration);
    }
    public static String color(String text) {
        return colorService.color(text);
    }
    public static Color getColor(String abbr) {
        return colorService.getColor(abbr);
    }
}
