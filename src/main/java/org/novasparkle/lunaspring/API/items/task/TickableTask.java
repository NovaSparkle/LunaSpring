package org.novasparkle.lunaspring.API.items.task;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.novasparkle.lunaspring.API.items.ComponentStorage;
import org.novasparkle.lunaspring.API.items.secondary.TimedItemComponent;
import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaTask;

import java.util.List;

@Getter
public class TickableTask extends LunaTask {
    private final Player player;
    public TickableTask(Player player) {
        super(0);
        this.player = player;
    }

    @Override @SneakyThrows
    @SuppressWarnings("all")
    public void start() {
        TaskManager.register(this);
        while (this.isActive() && TaskManager.check(this)) {
            Thread.sleep(1000L);

            PlayerInventory inventory = this.player.getInventory();
            ComponentStorage.getRealizedComponents(TimedItemComponent.class).forEach(c -> {
                List<ItemStack> list = ComponentStorage.scanInventory(inventory, c).toList();
                if (!list.isEmpty()) c.tick(this.player, list.stream());
            });
        }
    }

    public void stop() {
        this.cancel();
        TaskManager.unregister(this);
    }
}