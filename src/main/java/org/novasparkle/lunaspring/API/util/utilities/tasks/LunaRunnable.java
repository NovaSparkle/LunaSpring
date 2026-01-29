package org.novasparkle.lunaspring.API.util.utilities.tasks;

import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

public interface LunaRunnable {
    int getTaskId();
    void start();
    default void cancel() {
        TaskManager.unregister(this);
        try {
            int id = this.getTaskId();
            Utils.cancelTask(id);
        } catch (IllegalStateException ignored) {}
    }
}
