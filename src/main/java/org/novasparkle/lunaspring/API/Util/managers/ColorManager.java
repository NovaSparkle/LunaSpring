package org.novasparkle.lunaspring.API.Util.managers;

import lombok.Getter;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.API.Util.utilities.Color;
import org.novasparkle.lunaspring.API.Util.Service.realized.ColorService;
import org.novasparkle.lunaspring.API.Util.utilities.Utils;
import org.novasparkle.lunaspring.self.ConfigManager;


public class ColorManager {
    @Getter
    private static ColorService colorService;
    public static void init(ColorService service) {
        colorService = service;
        LunaSpring.getServiceProvider().register(service);
        LunaSpring.getINSTANCE().info(ConfigManager.getMessage("colorManager"));
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
