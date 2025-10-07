package org.novasparkle.lunaspring.self.messageActions;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.util.utilities.ComponentUtils;
import org.novasparkle.lunaspring.self.messageActions.abs.DefaultMessageAction;
import org.novasparkle.lunaspring.self.messageActions.abs.MessageAction;

@MessageAction("HOVER")
public class HoverTextMessageAction extends DefaultMessageAction {
    @Override
    public void execute(CommandSender target, String line) {
        target.spigot().sendMessage(ComponentUtils.createHoverableText(line));
    }
}
