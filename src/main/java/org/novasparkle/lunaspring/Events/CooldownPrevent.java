package org.novasparkle.lunaspring.Events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CooldownPrevent<T> {
    private final Map<T, Long> cooldownMap = new HashMap<>();
    @Setter private int cooldownMS = 0;

    public boolean cancelEvent(Cancellable e, T object) {
        if (this.cooldownMS <= 0) return false;

        if (this.cooldownMap.containsKey(object) && this.cooldownMap.get(object) < System.currentTimeMillis()) {
            e.setCancelled(true);
            return true;
        } else {
            this.cooldownMap.put(object, System.currentTimeMillis() + this.cooldownMS);
            return false;
        }
    }
}
