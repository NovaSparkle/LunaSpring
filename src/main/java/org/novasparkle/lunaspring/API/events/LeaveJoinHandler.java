package org.novasparkle.lunaspring.API.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.novasparkle.lunaspring.API.eventManagment.managers.LunaEventManager;

public class LeaveJoinHandler implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        LunaEventManager.getActiveEvents().forEach(a -> {
            if (a.getEventBar() != null) a.getEventBar().getBossBar().addPlayer(player);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        LunaEventManager.getActiveEvents().forEach(a -> {
            if (a.getEventBar() != null) a.getEventBar().getBossBar().removePlayer(player);
        });
    }
}
