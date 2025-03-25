package org.novasparkle.lunaspring;

import lombok.Getter;
import org.novasparkle.lunaspring.API.Util.Service.ServiceProvider;
import org.novasparkle.lunaspring.API.Util.Service.realized.ColorService;
import org.novasparkle.lunaspring.self.Command;
import org.novasparkle.lunaspring.self.ConfigManager;

public final class LunaSpring extends LunaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    @Getter
    private static final ServiceProvider serviceProvider = new ServiceProvider();

    public void onEnable() {
        INSTANCE = this;

        this.saveDefaultConfig();
        this.registerTabExecutor(new Command(), "lunaspring");

        ColorService service = new ColorService(this.getConfig());
        serviceProvider.register(service);
        ConfigManager.init(service);
    }
}



