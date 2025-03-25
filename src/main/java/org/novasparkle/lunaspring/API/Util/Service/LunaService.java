package org.novasparkle.lunaspring.API.Util.Service;

import org.novasparkle.lunaspring.LunaSpring;

public interface LunaService {
    default void exceptionCheck(LunaService service, Class<?> serviceClass) {
        if (service == null || (!LunaSpring.getServiceProvider().isRegistered(service.getClass()))) {
            throw new ServiceRegistrationException(serviceClass);
        }
    }
}