package org.novasparkle.lunaspring.API.util.service.managers;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.util.utilities.LunaTask;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.API.util.utilities.lists.LunaList;
import org.novasparkle.lunaspring.API.util.utilities.lists.LunaLists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@UtilityClass
public class TaskManager {
    @Getter private final List<Integer> tasksId = Lists.newArrayList();
    @Getter private final LunaList<LunaTask> tasks = LunaLists.newList();

    public void register(LunaTask task) {
        tasks.add(task);
    }

    public void register(int id) {
        tasksId.add(id);
    }

    public void unregister(LunaTask task) {
        tasks.remove(task);
    }

    public void unregister(Integer id) {
        tasksId.remove(id);
    }

    public boolean check(LunaTask task) {
        return tasks.contains(task);
    }

    public void stopAll() {
        tasksId.forEach(Utils::cancelTask);
        tasksId.clear();

        new ArrayList<>(tasks).forEach(LunaTask::cancel);
        tasks.clear();
    }

    public <T extends LunaTask> Optional<T> get(Class<T> clazz, Predicate<T> predicate) {
        return tasks.s().filter(t -> clazz.isAssignableFrom(t.getClass())).map(clazz::cast).filter(predicate).findFirst();
    }
}