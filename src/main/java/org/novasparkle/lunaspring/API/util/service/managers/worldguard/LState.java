package org.novasparkle.lunaspring.API.util.service.managers.worldguard;

import com.sk89q.worldguard.protection.flags.StateFlag;
import lombok.Getter;

@Getter
public enum LState {
    ALLOW(StateFlag.State.ALLOW),
    DENY(StateFlag.State.DENY);

    private final StateFlag.State state;
    LState(StateFlag.State state) {
        this.state = state;
    }
}
