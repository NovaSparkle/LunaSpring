package org.novasparkle.lunaspring.API.util.exceptions;

import org.novasparkle.lunaspring.LunaSpring;

public abstract class LunaException extends RuntimeException {
    public LunaException(String message) {
        super(String.format("LunaSpring v%s exception: %s", LunaSpring.getInstance().getVersion(), message));
    }
}
