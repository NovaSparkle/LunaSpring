package org.novasparkle.lunaspring;

import lombok.Getter;
import org.novasparkle.lunaspring.API.commands.LunaSpringCommandProcessor;
import org.novasparkle.lunaspring.API.drops.DropEvent;
import org.novasparkle.lunaspring.API.drops.managers.LunaDropManager;
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

        this.registerListeners(new MenuHandler());
        this.registerCommandProcessor(new LunaSpringCommandProcessor(this, "lunaspring"));

        this.createPlaceholder(((offlinePlayer, params) -> {
            // drops_next_time/name | drops_next_left_time | drops_left_time | drops_active | drops_now |
            if (params.equalsIgnoreCase("drops_next_time")) {
                return LunaDropManager.getNextTime().toString();
            }
            else if (params.equalsIgnoreCase("drops_next_name")) {
                return ColorManager.color(LunaDropManager.getNext().getName());
            }
            else if (params.equalsIgnoreCase("drops_next_left_time")) {
                return LunaDropManager.getLeftTime(LunaDropManager.getNext()).toString();
            }
            else if (params.equalsIgnoreCase("drops_active")) {
                return LunaDropManager.getActiveEvents().isEmpty() ? "no" : "yes";
            }
            else if (params.equalsIgnoreCase("drops_now")) {
                DropEvent dropEvent = LunaDropManager.getActiveEvent();
                return dropEvent == null ? LSConfig.getMessage("dropNotActive") :
                        ColorManager.color(LunaDropManager.getDropManager(dropEvent.getLunaPlugin()).getName());
            }
            else if (params.equalsIgnoreCase("drops_left_time")) {
                DropEvent dropEvent = LunaDropManager.getActiveEvent();
                return dropEvent == null ? LSConfig.getMessage("dropNotActive") : Utils.parseTime(dropEvent.getDelay().getLeftSeconds()).toString();
            }
            else if (params.equalsIgnoreCase("hooked")) {
                return String.join(", ", this.hookedPlugins.stream().map(LunaPlugin::getName).toList());
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



