package org.novasparkle.lunaspring.self.messageActions;

import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.messageActions.MessageAction;
import org.novasparkle.lunaspring.API.messageActions.PlayerMessageAction;

@MessageAction("ACTION_BAR")
public class ActionBarMessageAction extends PlayerMessageAction {
    @Override
    public void execute(Player target, String line) {
        target.sendActionBar(ColorManager.color(line));
    }
}
