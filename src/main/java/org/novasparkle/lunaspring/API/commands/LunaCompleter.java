package org.novasparkle.lunaspring.API.commands;

import org.bukkit.command.CommandSender;

import java.util.List;


public interface LunaCompleter extends Invocation {
    List<String> tabComplete(CommandSender sender, List<String> subCommandArgs);
}