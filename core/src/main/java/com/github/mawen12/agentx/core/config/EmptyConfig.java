package com.github.mawen12.agentx.core.config;

import com.github.mawen12.agentx.api.config.Config;

import java.util.Collections;
import java.util.Map;

public enum EmptyConfig implements Config {
    INSTANCE;

    @Override
    public String getString(String key) {
        return null;
    }

    @Override
    public boolean hasConfig() {
        return false;
    }

    @Override
    public Map<String, String> sources() {
        return Collections.emptyMap();
    }


}
