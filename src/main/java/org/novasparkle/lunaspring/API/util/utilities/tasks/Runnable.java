package org.novasparkle.lunaspring.API.util.utilities.tasks;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;
public class Runnable extends BukkitRunnable implements LunaRunnable {
    private final java.lang.Runnable runnable;
    private boolean isRepeatingTask = false;

    public Runnable(java.lang.Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        if (this.isCancelled()) return;
        this.start();
    }

    @Override
    public synchronized @NotNull BukkitTask runTaskTimer(@NotNull Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        isRepeatingTask = true;
        return super.runTaskTimer(plugin, delay, period);
    }

    @Override
    public synchronized @NotNull BukkitTask runTaskTimerAsynchronously(@NotNull Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        isRepeatingTask = true;
        return super.runTaskTimerAsynchronously(plugin, delay, period);
    }

    @Override
    public void start() {
        if (isRepeatingTask) {
            this.runnable.run();
            return;
        }

        try {
            TaskManager.register(this);
            this.runnable.run();
        } finally {
            LunaRunnable.super.cancel();
        }
    }

    public static Runnable start(java.lang.Runnable runnable) {
        return new Runnable(runnable);
    }
}