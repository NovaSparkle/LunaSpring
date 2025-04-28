package org.novasparkle.lunaspring;

import lombok.Getter;
import org.novasparkle.lunaspring.API.commands.LunaSpringCommandProcessor;
import org.novasparkle.lunaspring.API.drops.LunaEvent;
import org.novasparkle.lunaspring.API.drops.managers.LunaEventManager;
import org.novasparkle.lunaspring.API.events.DropHandler;
import org.novasparkle.lunaspring.API.events.MenuHandler;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.self.LSConfig;

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

        this.registerListeners(new MenuHandler(), new DropHandler());
        this.registerCommandProcessor(new LunaSpringCommandProcessor(this, "lunaspring"));

        this.createPlaceholder(((offlinePlayer, params) -> {
            if (params.equalsIgnoreCase("drops_next_time")) {
                return LunaEventManager.getNextTime().toString(); // Время след. ивента
            }
            else if (params.equalsIgnoreCase("drops_next_name")) {
                return ColorManager.color(LunaEventManager.getNext().getName()); // Имя след. ивента
            }
            else if (params.equalsIgnoreCase("drops_next_left_time")) {
                return LunaEventManager.getLeftTime(LunaEventManager.getNext()).toString(); // Время до след. ивента
            }
            else if (params.equalsIgnoreCase("drops_active")) {
                return LunaEventManager.getActiveEvents().isEmpty() ? "no" : "yes"; // Активен ли сейчас любой ивент
            }
            else if (params.equalsIgnoreCase("drops_now")) {
                LunaEvent lunaEvent = LunaEventManager.getActiveEvent();
                return lunaEvent == null ? LSConfig.getMessage("dropNotActive") :
                        ColorManager.color(LunaEventManager.getDropManager(lunaEvent.getLunaPlugin()).getName()); // Имя активного ивента
            }
            else if (params.equalsIgnoreCase("drops_left_time")) {
                LunaEvent lunaEvent = LunaEventManager.getActiveEvent();
                return lunaEvent == null ? LSConfig.getMessage("dropNotActive") : Utils.parseTime(lunaEvent.getDelay().getLeftSeconds()).toString();
                // Время, которое осталось до окончания ивента
            }
            else if (params.equalsIgnoreCase("hooked")) {
                return String.join(", ", this.hookedPlugins.stream().map(LunaPlugin::getName).toList()); // Список луна плагинов
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



