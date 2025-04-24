package org.novasparkle.lunaspring.API.util.service.managers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.API.util.utilities.Color;
import org.novasparkle.lunaspring.API.util.service.ColorService;

/**
 * Менеджер для управления кастомными цветами. Подробнее в <a href="https://github.com/NovaSparkle/LunaSpring/wiki/Глава-V.-Сервисы-и-Менеджеры#colorservice--colormanager">документации</a>.
 */
public class ColorManager {
    @Getter
    @NotNull
    private final static ColorService colorService;
    static {
        colorService = new ColorService(LunaSpring.getINSTANCE().getConfig());
    }

    /**
     * Поскраска текста.
     */
    public static String color(String text) {
        return colorService.color(text);
    }

    /**
     * Получить цвет по аббревиатуре.
     */
    public static Color getColor(String abbr) {
        return colorService.getColor(abbr);
    }
}