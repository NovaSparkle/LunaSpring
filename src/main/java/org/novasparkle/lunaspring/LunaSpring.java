package org.novasparkle.lunaspring;

import lombok.Getter;
import org.novasparkle.lunaspring.Util.Service.ServiceProvider;

public final class LunaSpring extends LunaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    @Getter
    private static final ServiceProvider serviceProvider = new ServiceProvider();

    public void onEnable() {
        INSTANCE = this;
        this.saveDefaultConfig();
        this.registerTabExecutor(new Command(), "lunaspring");
    }
}



