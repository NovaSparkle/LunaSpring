package org.novasparkle.lunaspring.Util.managers;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Color;
import org.novasparkle.lunaspring.Util.Service.realized.ColorService;
import org.novasparkle.lunaspring.Util.Service.ServiceRegistrationException;
import org.novasparkle.lunaspring.Util.Utils;

@UtilityClass
public class ColorManager {
    @Getter
    private ColorService colorService;
    public void init(ColorService service) {
        colorService = service;
    }
    public String color(String text) {
        if (colorService == null || (!LunaSpring.getServiceProvider().isRegistered(ColorManager.getColorService().getClass()))) {
            return Utils.color(text);
        }
        return colorService.color(text);
    }
    public Color getColor(String abbr) {
        if (colorService == null || (!LunaSpring.getServiceProvider().isRegistered(ColorManager.getColorService().getClass()))) {
            throw new ServiceRegistrationException(ColorManager.class);
        }
        return colorService.getColor(abbr);
    }
}
