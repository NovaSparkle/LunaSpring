package org.novasparkle.lunaspring.self.messageActions;

import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.util.utilities.AnnounceUtils;
import org.novasparkle.lunaspring.API.messageActions.MessageAction;
import org.novasparkle.lunaspring.API.messageActions.PlayerMessageAction;

@MessageAction("SOUND")
public class SoundMessageAction extends PlayerMessageAction {
    @Override
    public void execute(Player target, String line) {
        AnnounceUtils.sound(target, line);
    }
}
