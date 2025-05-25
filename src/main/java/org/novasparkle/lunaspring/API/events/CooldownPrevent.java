package org.novasparkle.lunaspring.API.events;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.event.Cancellable;

import java.util.HashMap;
import java.util.Map;


/**
 * CooldownPrevent нужен для создания задержек между разными событиями (например, клик по слоту инвентаря и т.д.).
 * @param <T> - класс по которому определяется задержка.
 */
@Getter
public class CooldownPrevent<T> implements Cloneable {
    private final Map<T, Long> cooldownMap = new HashMap<>();
    @Setter private int cooldownMS;

    public CooldownPrevent() {
        this(0);
    }

    public CooldownPrevent(int cooldownMS) {
        this.cooldownMS = cooldownMS;
    }

    /**
     * Возвращает true если определилось то, что object всё еще недоступен, и false если время задержки истекло или для object нет задержек
     * (если время задержки не 0, то при получении false, для object создаётся задержка).
     * Если вернуло true и event не равен null - будет автоматическая отмена самого ивента, если в этом нет необходимости - передавайте в первый аргумент null.
     * @param event ивент, требующий отмены.
     * @param object объект для проверки на задержку.
     */
    public boolean isCancelled(Cancellable event, T object) {
        if (this.cooldownMS <= 0) return false;

        if (this.cooldownMap.containsKey(object) && this.cooldownMap.get(object) >= System.currentTimeMillis()) {
            if (event != null) event.setCancelled(true);
            return true;
        } else {
            this.cooldownMap.put(object, System.currentTimeMillis() + this.cooldownMS);
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public CooldownPrevent<T> clone() {
        return (CooldownPrevent<T>) super.clone();
    }
}
