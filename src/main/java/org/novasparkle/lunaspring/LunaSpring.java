package org.novasparkle.lunaspring;

import lombok.Getter;
import org.novasparkle.lunaspring.API.commands.LunaSpringCommandProcessor;
import org.novasparkle.lunaspring.API.events.MenuHandler;

import java.util.HashSet;
import java.util.Set;

public final class LunaSpring extends LunaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    @Getter
    private final Set<LunaPlugin> hookedPlugins = new HashSet<>();

    @Override
    public void onEnable() {
        super.onEnable();
        INSTANCE = this;
        this.saveDefaultConfig();

        this.registerListeners(new MenuHandler());
        this.registerTabExecutor(new LunaSpringCommandProcessor(this), "lunaspring");
    }

    public void hookPlugin(LunaPlugin lunaPlugin) {
        if (lunaPlugin != INSTANCE) {
            hookedPlugins.add(lunaPlugin);
        }
    }

    public LunaPlugin getLunaPlugin(String name) {
        return this.hookedPlugins.stream().filter(pl -> pl.getName().equals(name)).findFirst().orElse(null);
    }
}



