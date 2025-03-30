package org.novasparkle.lunaspring;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.novasparkle.lunaspring.API.Events.MenuHandler;
import org.novasparkle.lunaspring.self.Command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class LunaSpring extends LunaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    @Getter
    private final Set<LunaPlugin> hookedPlugins = new HashSet<>();
    @Override
    public void onEnable() {
        INSTANCE = this;
        this.registerListeners(new MenuHandler());
        this.saveDefaultConfig();
        this.registerTabExecutor(new Command(), "lunaspring");
    }

    public void hookPlugin(LunaPlugin lunaPlugin) {
        if (lunaPlugin != INSTANCE) {
            hookedPlugins.add(lunaPlugin);
        }
    }
}



