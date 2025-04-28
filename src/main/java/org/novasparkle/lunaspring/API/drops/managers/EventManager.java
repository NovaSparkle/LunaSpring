package org.novasparkle.lunaspring.API.drops.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.novasparkle.lunaspring.API.drops.LunaEvent;
import org.novasparkle.lunaspring.LunaPlugin;

import java.util.List;

@RequiredArgsConstructor @Getter
public abstract class EventManager {
    private final LunaPlugin lunaPlugin;
    private final List<String> times;
    private final String name;
    private LunaEvent running;

    public boolean rawRun(LunaEvent lunaEvent) {
        this.running = lunaEvent;
        return lunaEvent.start();
    }

    public boolean run(LunaEvent lunaEvent) {
        if (this.isActive()) return false;
        return this.rawRun(lunaEvent);
    }

    public abstract boolean run();

    public boolean isActive() {
        return this.running != null && this.running.isStarted();
    }

    public void stop() {
        if (this.running != null) {
            this.running.stop();
            this.running = null;
        }
    }
}
