package org.novasparkle.lunaspring.Util.Service;

import java.util.HashSet;
import java.util.Set;

public class ServiceProvider {
    private final Set<LunaService> services = new HashSet<>();
    public void register(LunaService service) {
        this.services.add(service);
    }
    public boolean isRegistered(Class<?> clazz) {
        return this.services.stream().anyMatch(s -> s.getClass().isInstance(clazz));
    }
    public void unregister(LunaService service) {
        this.services.remove(service);
    }
}
