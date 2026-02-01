package org.novasparkle.lunaspring.API.util.modules;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.service.managers.VanishManager;
import org.novasparkle.lunaspring.API.util.service.managers.VaultManager;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.Arrays;
import java.util.function.Supplier;

import static org.bukkit.Bukkit.getServer;

@UtilityClass
public class Modules {
    public <E extends LunaModule> boolean register(E realizedModule) {
        Class<?> serviceClass = Arrays.stream(realizedModule.getClass().getInterfaces())
                .filter(LunaModule.class::isAssignableFrom)
                .findFirst()
                .orElse(null);
        if (serviceClass == null) return false;

        Bukkit.getServer().getServicesManager().register(
                (Class<LunaModule>) serviceClass,
                realizedModule,
                realizedModule.getOwnPlugin(),
                ServicePriority.Highest
        );
        return true;
    }

    public <E> E provide(Class<E> targetClass, @NotNull Supplier<E> ifProviderIsNullConsumer) {
        RegisteredServiceProvider<E> registeredServiceProvider = getServer().getServicesManager().getRegistration(targetClass);
        if (registeredServiceProvider == null) {
            return ifProviderIsNullConsumer.get();
        }
        else {
            return registeredServiceProvider.getProvider();
        }
    }

    public <E> E provide(Class<E> targetClass) {
        return provide(targetClass, () -> null);
    }

    public void initializeServices(LunaSpring lunaSpring) {
        Bukkit.getScheduler().runTask(lunaSpring, () -> {
            VaultManager.initialize();
            VanishManager.initialize();
        });
    }
}
