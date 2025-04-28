package org.novasparkle.lunaspring.API.drops.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.novasparkle.lunaspring.API.drops.DropEvent;
import org.novasparkle.lunaspring.LunaPlugin;

import java.util.List;

@RequiredArgsConstructor @Getter
public abstract class DropManager {
    private final LunaPlugin lunaPlugin;
    private final List<String> times;
    private final String name;
    private DropEvent running;

    public boolean rawRun(DropEvent dropEvent) {
        this.running = dropEvent;
        return dropEvent.start();
    }

    public boolean run(DropEvent dropEvent) {
        if (this.isActive()) return false;
        return this.rawRun(dropEvent);
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
