package org.novasparkle.lunaspring.API.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Интерфейс, описывающий логику автозаполенения команды<br><br>
 * <b>Сохраняет индексацию аргументов!</b><br>
 * То есть в SubCommand args.get(0) == идентификатор подкоманды
 */
public interface LunaCompleter {
    List<String> tabComplete(CommandSender sender, List<String> args);
}