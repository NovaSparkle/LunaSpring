package org.novasparkle.lunaspring.self.messageActions;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.util.utilities.AnnounceUtils;
import org.novasparkle.lunaspring.API.messageActions.DefaultMessageAction;
import org.novasparkle.lunaspring.API.messageActions.MessageAction;

@MessageAction("SOUND_ALL")
public class SoundAllMessageAction extends DefaultMessageAction {
    @Override
    public void execute(CommandSender target, String line) {
        AnnounceUtils.soundAll(line);
    }
}
