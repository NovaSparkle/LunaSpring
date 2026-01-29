package org.novasparkle.lunaspring.API.menus.updatable.tasks;

import org.novasparkle.lunaspring.API.menus.updatable.UpdatableIMenu;
import org.novasparkle.lunaspring.API.menus.updatable.UpdatableItem;

import java.util.Collection;

public class OptimizedUpdatableTask extends UpdatableTask {

    private final Collection<UpdatableItem> updatableItems;


    public OptimizedUpdatableTask(UpdatableIMenu updatableIMenu, int tickDelay) {
        super(updatableIMenu, tickDelay);
        this.updatableItems = super.getItems();
    }

    public OptimizedUpdatableTask(UpdatableIMenu updatableIMenu, int tickDelay, boolean reInsert) {
        super(updatableIMenu, tickDelay, reInsert);
        this.updatableItems = super.getItems();
    }

    @Override
    protected Collection<UpdatableItem> getItems() {
        return this.updatableItems;
    }
}
