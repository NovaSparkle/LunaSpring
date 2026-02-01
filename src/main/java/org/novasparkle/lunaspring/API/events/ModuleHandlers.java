package org.novasparkle.lunaspring.API.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.novasparkle.lunaspring.API.util.service.managers.VanishManager;

public class ModuleHandlers implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        VanishManager.processJoinHandler(e);
    }
}
