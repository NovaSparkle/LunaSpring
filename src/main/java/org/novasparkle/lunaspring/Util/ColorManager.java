package org.novasparkle.lunaspring.Util;

import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.ColorService;

public class ColorManager {
    protected static ColorService colorService;
    public static void init(ColorService service) {
        colorService = service;
    }
    public static String color(String text) {
        if (colorService == null || (!LunaSpring.getProvider().isRegistered(ColorManager.class))) {
            return Utils.color(text);
        }
        return colorService.color(text);
    }
    public static Color getColor(String abbr) {
        if (colorService == null || (!LunaSpring.getProvider().isRegistered(ColorManager.class))) {
            throw new ServiceRegistrationException(ColorManager.class);
        }
        return colorService.getColor(abbr);
    }
}
