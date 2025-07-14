package org.novasparkle.lunaspring.API.util.service.managers.worldguard;

import com.sk89q.worldguard.protection.flags.StateFlag;
import lombok.Getter;

@Getter
public enum LState {
    ALLOW,
    DENY;

    public StateFlag.State getWGState() {
        return this == ALLOW ? StateFlag.State.ALLOW : StateFlag.State.DENY;
    }
}
