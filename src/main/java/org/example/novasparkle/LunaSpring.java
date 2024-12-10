package org.example.novasparkle;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.novasparkle.Events.MenuHandler;

public final class LunaSpring extends JavaPlugin {

    @Override
    public void onEnable() {
        this.registerEvent(new MenuHandler());

    }

    @Override
    public void onDisable() {

    }
    private void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
}