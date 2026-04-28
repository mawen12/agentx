package com.github.mawen12.agentx.core.config;

import com.github.mawen12.agentx.api.config.Config;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

@ToString
@AllArgsConstructor
public class ConfigImpl implements Config {

    private final Map<String, String> source;

    @Override
    public String getString(String key) {
        return source.get(key);
    }

    @Override
    public boolean hasConfig() {
        return source != null && !source.isEmpty();
    }

    @Override
    public Map<String, String> sources() {
        return source;
    }

    public void mergeConfigs(Map<String, String> configs) {
        source.putAll(configs);
    }

    public void mergeConfigs(Config config) {
        if (config != null && config.hasConfig()) {
            source.putAll(config.sources());
        }
    }
}
