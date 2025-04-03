package org.novasparkle.lunaspring;

import lombok.Getter;
import org.novasparkle.lunaspring.API.Events.MenuHandler;
import org.novasparkle.lunaspring.self.LSCommand;
import org.novasparkle.lunaspring.self.PluginsCommand;

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
        this.registerTabExecutor(new LSCommand(), "lunaspring");
        this.registerCommand(new PluginsCommand(), "plugins");
    }

    public void hookPlugin(LunaPlugin lunaPlugin) {
        if (lunaPlugin != INSTANCE) {
            hookedPlugins.add(lunaPlugin);
        }
    }
}



