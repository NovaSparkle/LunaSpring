package org.novasparkle.lunaspring;

import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.Util.Service.ServiceProvider;

public final class LunaSpring extends JavaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    @Getter
    private final ServiceProvider provider = new ServiceProvider();
    @Override
    public void onEnable() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {

    }

    private void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
}
