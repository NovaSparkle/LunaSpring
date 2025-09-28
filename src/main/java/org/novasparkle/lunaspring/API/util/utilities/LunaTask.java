package org.novasparkle.lunaspring.API.util.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;

import java.util.ArrayList;
import java.util.List;

@Getter @RequiredArgsConstructor
public abstract class LunaTask extends BukkitRunnable {
    private final long ticks;
    private boolean isActive;

    public abstract void start();

    @Override
    public void run() {
        if (this.isActive) return;
        TaskManager.register(this);

        this.isActive = true;
        this.start();
    }

    public void cancel() {
        if (!this.isActive) return;
        this.isActive = false;

        int id = this.getTaskId();
        TaskManager.unregister(this);

        Utils.cancelTask(id);
    }
}
