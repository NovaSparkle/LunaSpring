package org.novasparkle.lunaspring.API.util.utilities.tasks;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;

@Getter
public abstract class LunaTask extends BukkitRunnable implements LunaRunnable {
    private final long ticks;
    private boolean isActive;

    public LunaTask(long ticks) {
        this.ticks = ticks;
    }

    public LunaTask() {
        this(0);
    }

    @Override
    public void run() {
        if (this.isActive) return;

        TaskManager.register(this);
        this.isActive = true;

        try {
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
            this.cancel();
        } finally {
            this.cancel();
        }
    }

    public void cancel() {
        if (!this.isActive) return;
        this.isActive = false;

        LunaRunnable.super.cancel();
    }
}
