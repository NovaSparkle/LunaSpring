package org.novasparkle.lunaspring.Util;

import lombok.Getter;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.ColorService;

public class ColorManager {
    @Getter
    protected static ColorService colorService;
    public static void init(ColorService service) {
        colorService = service;
    }
    public static String color(String text) {
        if (colorService == null || (!LunaSpring.getProvider().isRegistered(ColorManager.getColorService().getClass()))) {
            return Utils.color(text);
        }
        return colorService.color(text);
    }
    public static Color getColor(String abbr) {
        System.out.println(colorService);
        if (colorService == null || (!LunaSpring.getProvider().isRegistered(ColorManager.getColorService().getClass()))) {
            throw new ServiceRegistrationException(ColorManager.class);
        }
        return colorService.getColor(abbr);
    }
}
