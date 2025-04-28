package org.novasparkle.lunaspring.self.commands.events;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.AppliedCommand;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.drops.LunaEvent;
import org.novasparkle.lunaspring.API.drops.managers.LunaEventManager;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.self.LSConfig;

@SubCommand(maxArgs = 1, commandIdentifiers = {"compass"}, flags = {LunaSpringSubCommand.AccessFlag.PLAYER_ONLY})
@AppliedCommand("event")
public class CompassSubCommand extends LunaSpringSubCommand {
    public CompassSubCommand(LunaPlugin plugin, int maxArgs, String[] commandIdentifiers, AccessFlag[] flags) {
        super(plugin, maxArgs, commandIdentifiers, flags);
    }

    @Override
    public void invoke(CommandSender sender, String[] args) {
        if (!checkCommand(sender, args, "lunaspring.event.compass")) return;

        Player player = (Player) sender;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() != Material.COMPASS) {
            LSConfig.sendMessage(player, "events.noCompass");
            return;
        }

        LunaEvent lunaEvent = LunaEventManager.getActiveEvent();
        if (lunaEvent == null) {
            LSConfig.sendMessage(sender, "events.inactive");
            return;
        }

        CompassMeta compassMeta = (CompassMeta) itemStack.getItemMeta();
        compassMeta.setLodestone(lunaEvent.getLocation());

        itemStack.setItemMeta(compassMeta);
        LSConfig.sendMessage(player, "events.compass");
    }
}
