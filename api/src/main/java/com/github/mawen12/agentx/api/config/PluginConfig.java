package com.github.mawen12.agentx.api.config;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class PluginConfig implements Config {

    private Config config;
    private String domain;
    private String component;
    private String signal;

    public String domain() {
        return domain;
    }

    public String component() {
        return component;
    }

    public String signal() {
        return signal;
    }

    @Override
    public String getString(String key) {
        return config.getString(key);
    }

    @Override
    public boolean hasConfig() {
        return config.hasConfig();
    }

    @Override
    public Map<String, String> sources() {
        return config.sources();
    }

    public boolean enabled() {
        String key = String.join(".", "plugin", domain, component, signal, "enabled");
        Boolean pluginEnabled = getBoolean(key);
        if (pluginEnabled != null && !pluginEnabled) {
            return false;
        }

        return globalEnabled();
    }

    public boolean globalEnabled() {
        String key = "plugin.global.enabled";
        return Objects.equals(getBoolean(key), true);
    }
}
