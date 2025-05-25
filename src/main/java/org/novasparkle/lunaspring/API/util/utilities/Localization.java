package org.novasparkle.lunaspring.API.util.utilities;

import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.LunaSpring;

public class Localization {
    public static <R extends Enum<R>> String localize(R type) {
        return new Configuration(LunaSpring.getINSTANCE().getDataFolder(), "localization").getString(String.format("%s.%s", type.getClass().getSimpleName(), type.name()));
    }
}
