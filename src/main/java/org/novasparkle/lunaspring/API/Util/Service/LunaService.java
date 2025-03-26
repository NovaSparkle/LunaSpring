package org.novasparkle.lunaspring.API.Util.Service;

import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;

public interface LunaService {
    default void register() {
        LunaSpring.getServiceProvider().register(this);
    }
    LunaPlugin getProvidingPlugin();
    default void exceptionCheck(LunaService service, Class<?> serviceClass) {
        if (service == null || (!LunaSpring.getServiceProvider().isRegistered(service.getClass()))) {
            throw new ServiceRegistrationException(serviceClass);
        }
    }
}