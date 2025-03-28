package org.novasparkle.lunaspring.API.Events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CooldownPrevent<T> {
    private final Map<T, Long> cooldownMap = new HashMap<>();
    @Setter private int cooldownMS;

    public CooldownPrevent() {
        this(0);
    }

    public CooldownPrevent(int cooldownMS) {
        this.cooldownMS = cooldownMS;
    }

    public boolean isCancelled(Cancellable e, T object) {
        if (this.cooldownMS <= 0) return false;

        if (this.cooldownMap.containsKey(object) && this.cooldownMap.get(object) >= System.currentTimeMillis()) {
            if (e != null) e.setCancelled(true);
            return true;
        } else {
            this.cooldownMap.put(object, System.currentTimeMillis() + this.cooldownMS);
            return false;
        }
    }
}
