package com.github.mawen12.agentx.core.config;

import com.github.mawen12.agentx.api.config.Config;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class ConfigImpl implements Config {

    private final Map<String, String> source;

    @Override
    public String getString(String key) {
        return source.get(key);
    }

    @Override
    public Integer getInt(String key) {
        String value = source.get(key);
        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean getBoolean(String key) {
        String value = source.get(key);
        if (value == null) {
            return null;
        }

        return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true");
    }

    @Override
    public Double getDouble(String key) {
        String value = source.get(key);
        if (value == null) {
            return null;
        }

        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Long getLong(String key) {
        String value = source.get(key);
        if (value == null) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return null;
        }
    }
}
