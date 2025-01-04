package org.novasparkle.lunaspring;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaSpring extends JavaPlugin {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    private void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
}
