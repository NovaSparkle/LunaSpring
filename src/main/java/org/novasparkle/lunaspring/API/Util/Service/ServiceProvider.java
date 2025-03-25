package org.novasparkle.lunaspring.API.Util.Service;

import lombok.Getter;
import org.novasparkle.lunaspring.API.Util.Service.realized.ColorService;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.ConfigManager;

import java.util.HashSet;
import java.util.Set;

@Getter
public final class ServiceProvider {
    private final Set<LunaService> services = new HashSet<>();

    public void register(LunaService service) {
        this.services.add(service);
        if (service.getClass() != ColorService.class)
            LunaSpring.getINSTANCE().info(ConfigManager.getMessage("serviceRegistered").replace("[service]", service.getClass().getSimpleName()));
    }
    public boolean isRegistered(Class<?> clazz) {
        return this.services.stream().anyMatch(s -> clazz.equals(s.getClass()));
    }
    public void unregister(LunaService service) {
        this.services.remove(service);
    }
}
