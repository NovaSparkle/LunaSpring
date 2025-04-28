package org.novasparkle.lunaspring.API.drops.managers;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.drops.DropEvent;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class LunaDropManager {
    @Getter private final Set<DropManager> managers = new HashSet<>();

    public boolean register(DropManager dropManager) {
        if (getDropManager(dropManager.getLunaPlugin()) == null) return managers.add(dropManager);
        return false;
    }

    public void unregister(DropManager dropManager) {
        managers.remove(dropManager);
    }

    public void unregister(LunaPlugin lunaPlugin) {
        DropManager dropManager = getDropManager(lunaPlugin);
        if (dropManager != null) unregister(dropManager);
    }

    public DropManager getDropManager(LunaPlugin lunaPlugin) {
        return managers.stream().filter(m -> m.getLunaPlugin().equals(lunaPlugin)).findFirst().orElse(null);
    }

    public boolean spawn(DropManager dropManager) {
        if (dropManager != null) return dropManager.run();
        return false;
    }

    public boolean spawn(DropManager dropManager, DropEvent dropEvent) {
        if (dropManager != null && dropEvent != null && dropEvent.getLunaPlugin().equals(dropManager.getLunaPlugin())) return dropManager.run(dropEvent);
        return false;
    }

    public boolean spawn() {
        List<DropManager> dropManagers = managers.stream().filter(d -> !d.isActive()).toList();
        if (dropManagers.isEmpty()) return false;

        DropManager dropManager = dropManagers.get(LunaMath.getRandomInt(0, dropManagers.size() - 1));
        return spawn(dropManager);
    }

    public boolean isTime(DropManager dropManager) {
        LocalTime localTime = Utils.getNextTime(dropManager.getTimes());

        LocalTime now = LocalTime.now();
        return localTime.getMinute() == now.getMinute() && localTime.getHour() == now.getHour();
    }

    public DropManager isTime() {
        return managers.stream().filter(m -> !m.isActive() && isTime(m)).findFirst().orElse(null);
    }

    public LocalTime getNextTime(DropManager dropManager) {
        return Utils.getNextTime(dropManager.getTimes());
    }

    public LocalTime getNextTime() {
        return getNextTime(getNext());
    }

    public DropManager getNext() {
        LocalTime now = LocalTime.now();
        return managers.stream().filter(m -> !m.isActive())
                .min((manager1, manager2) -> {
                    LocalTime nextTime1 = Utils.getNextTime(manager1.getTimes());
                    LocalTime nextTime2 = Utils.getNextTime(manager2.getTimes());

                    long diff1 = Math.min(
                            Math.abs(now.toSecondOfDay() - nextTime1.toSecondOfDay()),
                            24 * 60 * 60 - Math.abs(now.toSecondOfDay() - nextTime1.toSecondOfDay()));
                    long diff2 = Math.min(
                            Math.abs(now.toSecondOfDay() - nextTime2.toSecondOfDay()),
                            24 * 60 * 60 - Math.abs(now.toSecondOfDay() - nextTime2.toSecondOfDay()));

                    return Long.compare(diff1, diff2);
                })
                .orElse(null);
    }

    public LocalTime getLeftTime(DropManager dropManager) {
        return Utils.getTimeBetween(LocalTime.now(), getNextTime(dropManager));
    }

    public Set<DropEvent> getActiveEvents() {
        return managers.stream().filter(DropManager::isActive).map(DropManager::getRunning).collect(Collectors.toSet());
    }

    public DropEvent getActiveEvent() {
        return getActiveEvents().stream().findFirst().orElse(null);
    }
}
