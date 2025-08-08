package org.novasparkle.lunaspring.API.commands;

import java.util.List;

public record CommandReq(String[] permissions, ZeroArgCommand.AccessFlag[] accessFlags, List<String> tabCompleteIgnore, int maxArgs, int minArgs) {
    public CommandReq(String[] permissions, ZeroArgCommand.AccessFlag[] accessFlags, List<String> tabCompleteIgnore, int maxArgs, int minArgs) {
        this.permissions = permissions;
        this.accessFlags = accessFlags;
        this.tabCompleteIgnore = tabCompleteIgnore;
        this.maxArgs = maxArgs;
        this.minArgs = minArgs;
    }
}
