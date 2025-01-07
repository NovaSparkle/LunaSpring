package org.novasparkle.lunaspring.Util;

public class ServiceRegistrationException extends RuntimeException {
    public ServiceRegistrationException(Class<?> service) {
        super(String.format("Не удалось выполнить действие сервиса, потому что он не зарегистрирован: %s", service.getName()));
    }
}