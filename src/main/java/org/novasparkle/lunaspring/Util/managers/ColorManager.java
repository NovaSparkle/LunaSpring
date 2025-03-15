package org.novasparkle.lunaspring.Util.managers;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Color;
import org.novasparkle.lunaspring.Util.Service.realized.ColorService;
import org.novasparkle.lunaspring.Util.Utils;


public class ColorManager {
    @Getter
    private static ColorService colorService;
    public static void init(ColorService service) {
        colorService = service;
    }
    public static String color(String text) {
        if (colorService == null || (!LunaSpring.getServiceProvider().isRegistered(ColorManager.getColorService().getClass()))) {
            return Utils.color(text);
        }
        return colorService.color(text);
    }
    public static Color getColor(String abbr) {
        colorService.exceptionCheck(colorService, ColorManager.class);
        return colorService.getColor(abbr);
    }
}
