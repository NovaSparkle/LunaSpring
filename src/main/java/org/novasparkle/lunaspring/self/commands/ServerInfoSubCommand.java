package org.novasparkle.lunaspring.self.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Check;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.Arrays;
import java.util.List;

@SubCommand(commandIdentifiers = {"server-info"}, appliedCommand = "lunaspring")
@Check(permissions = {"lunaspring.server-info"}, flags = {})
public class ServerInfoSubCommand implements Invocation {

    @Override
    public void invoke(CommandSender sender, String[] args) {
        List<String> list = LSConfig.getStringList("server-info");

        int tps = (int) (Arrays.stream(Bukkit.getServer().getTPS()).sum() / Bukkit.getServer().getTPS().length);
        list.forEach(l -> {
            String line = l
                    .replace("[online]", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replace("[max-players]", String.valueOf(Bukkit.getMaxPlayers()))
                    .replace("[bukkit-version]", Bukkit.getBukkitVersion())
                    .replace("[mc-version]", Bukkit.getMinecraftVersion())
                    .replace("[port]", String.valueOf(Bukkit.getPort()))
                    .replace("[average_tps]", String.valueOf(tps));

            sender.sendMessage(ColorManager.color(line));
        });
    }
}
