package org.novasparkle.lunaspring.self.messageActions;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.util.utilities.ComponentUtils;
import org.novasparkle.lunaspring.API.messageActions.DefaultMessageAction;
import org.novasparkle.lunaspring.API.messageActions.MessageAction;

@MessageAction("RUNCOMMAND")
public class RunCommandMessageAction extends DefaultMessageAction {
    @Override
    public void execute(CommandSender target, String line) {
        target.spigot().sendMessage(ComponentUtils.createClickableText(line, ClickEvent.Action.RUN_COMMAND));
    }
}
