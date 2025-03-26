package org.novasparkle.lunaspring.API.Util.Service;

import lombok.Getter;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.ConfigManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public final class ServiceProvider {
    private final List<LunaService> services = new ArrayList<>();
    public void register(LunaService service) {
        this.services.add(service);
        LunaSpring.getINSTANCE().info(ConfigManager.getMessage("serviceRegistered")
                .replace("[service]", service.getClass().getSimpleName())
                .replace("[plugin]", service.getProvidingPlugin().getName()));
    }
    public boolean isRegistered(Class<?> clazz) {
        return this.services.stream().anyMatch(s -> clazz.equals(s.getClass()));
    }
    public void unregister(LunaService service) {
        this.services.remove(service);
    }
}
