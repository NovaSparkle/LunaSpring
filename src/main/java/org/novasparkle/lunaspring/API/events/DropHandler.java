package org.novasparkle.lunaspring.API.events;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.novasparkle.lunaspring.API.drops.managers.LunaDropManager;

public class DropHandler implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (LunaDropManager.getActiveEvents().stream().anyMatch(ev -> ev.getDropContainer().getBlock().equals(block))) e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null || block.getType().isAir()) return;

        LunaDropManager.getActiveEvents().forEach(ev -> {
            if (ev.getDropContainer().getBlock().equals(block)) ev.getDropContainer().onInteract(e);
        });
    }
}
