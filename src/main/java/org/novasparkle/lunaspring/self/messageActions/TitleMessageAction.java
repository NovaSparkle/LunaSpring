package org.novasparkle.lunaspring.self.messageActions;

import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.AnnounceUtils;
import org.novasparkle.lunaspring.self.messageActions.abs.MessageAction;
import org.novasparkle.lunaspring.self.messageActions.abs.PlayerMessageAction;

@MessageAction("TITLE")
public class TitleMessageAction extends PlayerMessageAction {
    @Override
    public void execute(Player target, String line) {
        AnnounceUtils.title(target, ColorManager.color(line));
    }
}
