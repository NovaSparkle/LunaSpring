package org.novasparkle.lunaspring.API.util.utilities;

import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;

@AllArgsConstructor
public class Runnable extends BukkitRunnable {
    private final java.lang.Runnable runnable;

    @Override
    public void run() {
        if (this.isCancelled()) return;

        TaskManager.register(this.getTaskId());
        this.runnable.run();
    }

    @Override
    public synchronized void cancel() {
        if (this.isCancelled()) return;

        TaskManager.unregister(this.getTaskId());
        super.cancel();
    }

    public static Runnable start(java.lang.Runnable runnable) {
        return new Runnable(runnable);
    }
}