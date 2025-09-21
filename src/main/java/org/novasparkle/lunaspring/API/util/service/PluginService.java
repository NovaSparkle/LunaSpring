package org.novasparkle.lunaspring.API.util.service;

import lombok.Getter;
import org.novasparkle.lunaspring.API.util.exceptions.NoProvidingPluginException;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

@Getter
public abstract class PluginService implements LunaService {
    private final String pluginName;
    public PluginService(String pluginName) {
        this.pluginName = pluginName;
    }

    public boolean mayUseService() {
        return Utils.isPluginEnabled(this.pluginName);
    }

    public void checkService() throws NoProvidingPluginException {
        if (!this.mayUseService()) throw new NoProvidingPluginException(this.pluginName);
    }
}
