package org.novasparkle.lunaspring.API.drops.managers;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.drops.LunaEvent;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class LunaEventManager {
    @Getter private final Set<EventManager> managers = new HashSet<>();

    public boolean register(EventManager eventManager) {
        if (getDropManager(eventManager.getLunaPlugin()) == null) return managers.add(eventManager);
        return false;
    }

    public void unregister(EventManager eventManager) {
        managers.remove(eventManager);
    }

    public void unregister(LunaPlugin lunaPlugin) {
        EventManager eventManager = getDropManager(lunaPlugin);
        if (eventManager != null) unregister(eventManager);
    }

    public EventManager getDropManager(LunaPlugin lunaPlugin) {
        return managers.stream().filter(m -> m.getLunaPlugin().equals(lunaPlugin)).findFirst().orElse(null);
    }

    public boolean spawn(EventManager eventManager) {
        if (eventManager != null) return eventManager.run();
        return false;
    }

    public boolean spawn(EventManager eventManager, LunaEvent lunaEvent) {
        if (eventManager != null && lunaEvent != null && lunaEvent.getLunaPlugin().equals(eventManager.getLunaPlugin())) return eventManager.run(lunaEvent);
        return false;
    }

    public boolean spawn() {
        List<EventManager> eventManagers = managers.stream().filter(d -> !d.isActive()).toList();
        if (eventManagers.isEmpty()) return false;

        EventManager eventManager = eventManagers.get(LunaMath.getRandomInt(0, eventManagers.size() - 1));
        return spawn(eventManager);
    }

    public boolean isTime(EventManager eventManager) {
        LocalTime localTime = Utils.getNextTime(eventManager.getTimes());

        LocalTime now = LocalTime.now();
        return localTime.getMinute() == now.getMinute() && localTime.getHour() == now.getHour();
    }

    public EventManager isTime() {
        return managers.stream().filter(m -> !m.isActive() && isTime(m)).findFirst().orElse(null);
    }

    public LocalTime getNextTime(EventManager eventManager) {
        return Utils.getNextTime(eventManager.getTimes());
    }

    public LocalTime getNextTime() {
        EventManager eventManager = getNext();
        return eventManager == null ? null : getNextTime(eventManager);
    }

    public EventManager getNext() {
        LocalTime now = LocalTime.now();
        return managers.stream()
                .min(Comparator.comparing(manager -> {
                    LocalTime nearestTime = Utils.getNextTime(manager.getTimes());

                    long diffInSeconds = Math.abs(nearestTime.toSecondOfDay() - now.toSecondOfDay());
                    return Math.min(diffInSeconds, 24 * 60 * 60 - diffInSeconds);
                }))
                .orElse(null);
    }

    public LocalTime getLeftTime(EventManager eventManager) {
        return eventManager == null ? null : Utils.getTimeBetween(LocalTime.now(), getNextTime(eventManager));
    }

    public Set<LunaEvent> getActiveEvents() {
        return managers.stream().filter(EventManager::isActive).map(EventManager::getRunning).collect(Collectors.toSet());
    }

    public LunaEvent getActiveEvent() {
        return getActiveEvents().stream().findFirst().orElse(null);
    }
}
