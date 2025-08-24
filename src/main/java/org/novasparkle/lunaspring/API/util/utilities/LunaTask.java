package org.novasparkle.lunaspring.API.util.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

@Getter @RequiredArgsConstructor
public abstract class LunaTask extends BukkitRunnable {
    @Getter
    private static final List<Integer> taskIds = new ArrayList<>();

    private final long ticks;
    private boolean isActive;

    public abstract void start();

    @Override
    public void run() {
        if (this.isActive) return;
        taskIds.add(this.getTaskId());

        this.isActive = true;
        this.start();
    }

    public void cancel() {
        if (!this.isActive) return;
        this.isActive = false;

        int id = this.getTaskId();
        taskIds.remove(id);

        BukkitScheduler scheduler = Bukkit.getScheduler();
        if (scheduler.isQueued(id) || scheduler.isCurrentlyRunning(id))
            scheduler.cancelTask(id);
    }
}
