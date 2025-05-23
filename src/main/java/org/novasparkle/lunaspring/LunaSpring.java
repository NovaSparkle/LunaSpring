package org.novasparkle.lunaspring;

import lombok.Getter;
import org.bukkit.Location;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.eventManagment.LunaEvent;
import org.novasparkle.lunaspring.API.eventManagment.managers.EventManager;
import org.novasparkle.lunaspring.API.eventManagment.managers.LunaEventManager;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.Color;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.self.LSConfig;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public final class LunaSpring extends LunaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    @Getter
    private final Set<LunaPlugin> hookedPlugins = new HashSet<>();

    @Override
    public void onEnable() {
        if (INSTANCE != null) super.onEnable();
        INSTANCE = this;
        this.saveDefaultConfig();

        this.processListeners();
        LunaExecutor.initialize(this);
        this.registerLunaPlaceholder();
    }

    private void registerLunaPlaceholder() {
        this.createPlaceholder(((offlinePlayer, params) -> {
            if (params.equalsIgnoreCase("hooked")) {
                return String.join(", ", this.hookedPlugins.stream().map(LunaPlugin::getName).toList()); // Список луна плагинов
            }

            if (params.startsWith("world-")) { // %lunaspring_world-world%
                String[] split = params.split("-");

                String placeholder = split.length >= 2 ? LSConfig.getMessage(String.format("worlds.%s", split[1])) : null;
                return placeholder == null || placeholder.isEmpty() ? (split.length == 1 ? null : split[1]) : placeholder;
            }

            if (params.startsWith("color-")) {
                String[] split = params.split("-");

                if (split.length >= 2) {
                    Color color = ColorManager.getColor(split[1]);
                    return color != null ? color.getVariable() : null;
                }
                return "";
            }

            if (params.startsWith("lp-")) { // %lunaspring_lp-SHORT_TRANSLATE_WITH_POINTS-DAYS_OR_HOURS%
                String[] split = params.split("-");
                if (split.length < 2) return null;

                Utils.Luckperms.TranslateType translateType = Utils.Luckperms.TranslateType.valueOf(split[1]);
                Utils.Luckperms.FormatType formatType = Utils.Luckperms.FormatType.valueOf(split[2]);
                return Utils.Luckperms.getFormatting(Utils.Luckperms.getGroupTime(offlinePlayer), translateType, formatType);
            }
            return null;
        }));

        this.createPlaceholder("event", ((offlinePlayer, params) -> {
            if (params.equalsIgnoreCase("next_time")) {
                LocalTime localTime = LunaEventManager.getNextTime();
                return localTime == null ? "no" : Utils.Time.timeToString(localTime); // Время след. ивента
            }

            if (params.equalsIgnoreCase("next_name")) {
                EventManager eventManager = LunaEventManager.getNext();
                return eventManager == null ? "no" : ColorManager.color(eventManager.getName()); // Имя след. ивента
            }

            if (params.equalsIgnoreCase("next_id")) {
                EventManager eventManager = LunaEventManager.getNext();
                return eventManager == null ? "no" : eventManager.getLunaPlugin().getName(); // Айди плагина след. ивента
            }

            if (params.equalsIgnoreCase("next_left_time")) {
                LocalTime localTime = LunaEventManager.getLeftTime(LunaEventManager.getNext());
                return localTime == null ? "no" : Utils.Time.timeToString(localTime); // Время до след. ивента
            }

            if (params.equalsIgnoreCase("active")) {
                return LunaEventManager.getActiveEvents().isEmpty() ? "no" : "yes"; // Активен ли сейчас любой ивент
            }

            LunaEvent lunaEvent = LunaEventManager.getActiveEvent();
            if (params.equalsIgnoreCase("now_name")) {
                return lunaEvent == null ? LSConfig.getMessage("dropNotActive") :
                        ColorManager.color(LunaEventManager.getManager(lunaEvent.getLunaPlugin()).getName()); // Имя активного ивента
            }

            if (params.equalsIgnoreCase("now_id")) {
                return lunaEvent == null ? LSConfig.getMessage("dropNotActive") : lunaEvent.getLunaPlugin().getName();
            }

            if (params.equalsIgnoreCase("left_time")) {
                return lunaEvent == null ? LSConfig.getMessage("dropNotActive") : Utils.Time.timeToString(
                        Utils.Time.parseTime(lunaEvent.getDelay().getLeftSeconds()));
                // Время, которое осталось до окончания ивента
            }

            if (params.endsWith("[a]")) {
                return lunaEvent == null ? Utils.setPlaceholders(offlinePlayer, "%event_" + params.replace("[a]", "") + "%") :
                        LSConfig.getMessage("dropIsActive"); // Проверка на активность ивента
            }

            Location location = lunaEvent == null ? null : lunaEvent.getLocation();
            if (params.equalsIgnoreCase("x")) {
                return location == null ? "---" : String.valueOf(location.getBlockX());
            }

            if (params.equalsIgnoreCase("y")) {
                return location == null ? "---" : String.valueOf(location.getBlockY());
            }

            if (params.equalsIgnoreCase("z")) {
                return location == null ? "---" : String.valueOf(location.getBlockZ());
            }

            if (params.equalsIgnoreCase("world")) {
                return location == null ? "---" : location.getWorld().getName();
            }
            return null;
        }));
    }

    public void hookPlugin(LunaPlugin lunaPlugin) {
        if (lunaPlugin != INSTANCE) this.hookedPlugins.add(lunaPlugin);
    }

    public LunaPlugin getLunaPlugin(String name) {
        return this.hookedPlugins.stream().filter(pl -> pl.getName().equals(name)).findFirst().orElse(null);
    }
}



