package org.novasparkle.lunaspring.API.util.service.managers;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.API.util.utilities.Color;
import org.novasparkle.lunaspring.API.util.service.ColorService;

/**
 * Менеджер для управления кастомными цветами. Подробнее в <a href="https://github.com/NovaSparkle/LunaSpring/wiki/Глава-V.-Сервисы-и-Менеджеры#colorservice--colormanager">документации</a>.
 */

@UtilityClass
public class ColorManager {
    @Getter @NotNull private final ColorService colorService;
    static {
        colorService = new ColorService(LunaSpring.getINSTANCE().getConfig());
    }

    /**
     * Поскраска текста.
     */
    public String color(String text) {
        return colorService.color(text);
    }

    /**
     * Получить цвет по аббревиатуре.
     */
    public Color getColor(@NonNull String abbr) {
        return colorService.getColor(abbr);
    }

    /**
     * Определяет, содержит ли строка только цвета, или нет
     * @param description - строка
     * @param color - цветовой символ, например &
     * @return true - если содержит только цвета
     */
    public boolean containsOnlyColor(String description, char color) {
        String regex = "^(§[0-9a-fk-orA-FK-OR])+?$";
        return description.replace(color, '§').matches(regex);
    }

    /**
     * Добавляет новый цвет color в список значений для использования, если color уже загружен через конфиг LunaSpring, то тот обновлён не будет
     * @param color - значение цвета
     * @return true - если новый color успешно добавлен
     */
    public boolean addColor(Color color) {
        return colorService.addColor(color);
    }
}