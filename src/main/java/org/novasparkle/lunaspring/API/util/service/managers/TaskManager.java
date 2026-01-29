package org.novasparkle.lunaspring.API.util.service.managers;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.util.utilities.tasks.LunaRunnable;
import org.novasparkle.lunaspring.API.util.utilities.tasks.LunaTask;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.API.util.utilities.lists.LunaList;
import org.novasparkle.lunaspring.API.util.utilities.lists.LunaLists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

@UtilityClass
public class TaskManager {
    @Getter private final List<Integer> tasksId = Lists.newArrayList();
    @Getter private final LunaList<LunaRunnable> tasks = LunaLists.newList();

    public void register(LunaRunnable task) {
        tasks.add(task);
    }

    public void register(int id) {
        tasksId.add(id);
    }

    public void unregister(LunaRunnable task) {
        tasks.remove(task);
    }

    public void unregister(Integer id) {
        tasksId.remove(id);
    }

    public boolean check(LunaRunnable task) {
        return tasks.contains(task);
    }

    public void stopAll() {
        tasksId.forEach(Utils::cancelTask);
        tasksId.clear();

        new ArrayList<>(tasks).forEach(LunaRunnable::cancel);
        tasks.clear();
    }

    public <T extends LunaRunnable> void stopAll(Class<T> clazz, Predicate<T> predicate) {
        List<T> runnables = getAll(clazz, predicate).toList();
        runnables.forEach(LunaRunnable::cancel);
        tasks.removeAll(runnables);
    }

    public <T extends LunaRunnable> Stream<T> getAll(Class<T> clazz, Predicate<T> predicate) {
        return tasks.s()
                .filter(t -> clazz.isAssignableFrom(t.getClass()))
                .map(clazz::cast)
                .filter(t -> predicate == null || predicate.test(t));
    }

    public <T extends LunaRunnable> Optional<T> get(Class<T> clazz, Predicate<T> predicate) {
        return getAll(clazz, predicate).findFirst();
    }
}