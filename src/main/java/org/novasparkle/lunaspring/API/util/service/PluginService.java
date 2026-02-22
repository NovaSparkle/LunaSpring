package org.novasparkle.lunaspring.API.util.service;

import lombok.Getter;
import org.novasparkle.lunaspring.API.util.exceptions.NoProvidingPluginException;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

@Getter
public abstract class PluginService implements LunaService {
    private final String pluginName;
    private final boolean enabled;
    public PluginService(String pluginName) {
        this.pluginName = pluginName;
        this.enabled = Utils.isPluginEnabled(this.pluginName);
    }

    public void checkService() throws NoProvidingPluginException {
        if (!this.enabled) throw new NoProvidingPluginException(this.pluginName);
    }
}
