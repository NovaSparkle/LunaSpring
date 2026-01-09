package org.novasparkle.lunaspring.self.messageActions.abs;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public abstract class PlayerMessageAction extends AbsMessageAction<Player> {
    @Override
    public boolean canCast(CommandSender sender) {
        return sender instanceof Player;
    }
}
