package org.novasparkle.lunaspring.API.util.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@Getter @RequiredArgsConstructor
public abstract class LunaTask extends BukkitRunnable {
    private final long ticks;
    private boolean isActive;

    public abstract void start();

    @Override
    public void run() {
        this.isActive = true;
        this.start();
    }

    public void cancel() {
        this.isActive = false;
    }
}
