package org.novasparkle.lunaspring.self.messageActions;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.AnnounceUtils;
import org.novasparkle.lunaspring.self.messageActions.abs.DefaultMessageAction;
import org.novasparkle.lunaspring.self.messageActions.abs.MessageAction;

@MessageAction("TITLE_ALL")
public class TitleAllMessageAction extends DefaultMessageAction {
    @Override
    public void execute(CommandSender target, String line) {
        AnnounceUtils.titleAll(ColorManager.color(line));
    }
}
