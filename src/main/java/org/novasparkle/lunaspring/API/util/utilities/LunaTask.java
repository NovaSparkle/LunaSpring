package org.novasparkle.lunaspring.API.util.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

@Getter @RequiredArgsConstructor
public abstract class LunaTask extends BukkitRunnable {
    private final long ticks;
    private boolean isActive;

    public abstract void start();

    @Override
    public void run() {
        if (this.isActive) return;

        this.isActive = true;
        this.start();
    }

    public void cancel() {
        if (!this.isActive) return;
        this.isActive = false;

        int id = this.getTaskId();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        if (scheduler.isQueued(id) || scheduler.isCurrentlyRunning(id))
            scheduler.cancelTask(id);
    }
}
