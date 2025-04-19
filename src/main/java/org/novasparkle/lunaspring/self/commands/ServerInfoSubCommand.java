package org.novasparkle.lunaspring.self.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.Commands.LunaSpringSubCommand;
import org.novasparkle.lunaspring.API.Util.Service.managers.ColorManager;
import org.novasparkle.lunaspring.self.LSConfig;

import java.util.Arrays;
import java.util.List;

public class ServerInfoSubCommand extends LunaSpringSubCommand {
    public ServerInfoSubCommand(String[] args, CommandSender sender) {
        super(args, 1, sender, false, "server-info");
    }

    @Override
    public boolean invoke() {
        if (noPermission()) return true;
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

            this.sender.sendMessage(ColorManager.color(line));
        });
        return true;
    }
}
