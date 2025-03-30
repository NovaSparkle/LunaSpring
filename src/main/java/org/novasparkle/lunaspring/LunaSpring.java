package org.novasparkle.lunaspring;

import lombok.Getter;
import lombok.SneakyThrows;
import org.novasparkle.lunaspring.API.Events.MenuHandler;
import org.novasparkle.lunaspring.self.Command;
import java.util.HashSet;
import java.util.Set;

public final class LunaSpring extends LunaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    private final Set<LunaPlugin> hookedPlugins = new HashSet<>();
    public LunaSpring() {
        super();
    }

    @SneakyThrows
    public void onEnable() {
        INSTANCE = this;
        this.registerListener(new MenuHandler());
        this.saveDefaultConfig();
        this.registerTabExecutor(new Command(), "lunaspring");
    }


    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    public void hookPlugin(LunaPlugin lunaPlugin) {
        if (lunaPlugin != INSTANCE) {
            hookedPlugins.add(lunaPlugin);
        }
    }

    public Set<LunaPlugin> showHookedPlugins() {
        return this.hookedPlugins;
    }
}



