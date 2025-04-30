package org.novasparkle.lunaspring.API.commands;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface Invocation {
    void invoke(CommandSender sender, String[] args);
}
