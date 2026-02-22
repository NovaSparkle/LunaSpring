package org.novasparkle.lunaspring.self.messageActions;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.messageActions.DefaultMessageAction;
import org.novasparkle.lunaspring.API.messageActions.MessageAction;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.API.util.utilities.tasks.Runnable;
import org.novasparkle.lunaspring.LunaSpring;

@MessageAction("DELAY")
public class DelayMessageAction extends DefaultMessageAction {
    @Override
    public void execute(CommandSender target, String line) {
        String[] split = line.split(" ");
        if (split.length <= 1) return;

        int delay = LunaMath.toInt(split[0], 10);
        Runnable.start(() -> {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < split.length; i++) builder.append(split[i]).append(" ");
            Utils.processCommandsWithActions(target, builder.toString());
        }).runTaskLaterAsynchronously(LunaSpring.getInstance(), delay);
    }
}
