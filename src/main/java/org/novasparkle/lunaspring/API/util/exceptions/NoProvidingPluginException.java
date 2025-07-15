package org.novasparkle.lunaspring.API.util.exceptions;

public class NoProvidingPluginException extends RuntimeException {
    public NoProvidingPluginException(String plugin) {
        super(String.format("Для работы, сервис требует плагин %s, но он отсутствует!", plugin));
    }
}
