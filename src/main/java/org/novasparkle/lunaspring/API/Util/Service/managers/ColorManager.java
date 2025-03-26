package org.novasparkle.lunaspring.API.Util.Service.managers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.API.Util.utilities.Color;
import org.novasparkle.lunaspring.API.Util.Service.ColorService;


public class ColorManager {
    @Getter
    @NotNull
    private final static ColorService colorService;
    static {
        colorService = new ColorService(LunaSpring.getINSTANCE().getConfig());
    }
    public static String color(String text) {
        return colorService.color(text);
    }
    public static Color getColor(String abbr) {
        return colorService.getColor(abbr);
    }
}