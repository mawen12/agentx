package com.github.mawen12.agentx.api.config;

import com.github.mawen12.agentx.api.utils.Null;

import java.util.Map;

public interface Config {

    String getString(String key);

    boolean hasConfig();

    Map<String, String> sources();

    default String getString(String key, String defaultVal) {
        return Null.of(getString(key), defaultVal);
    }

    default Integer getInt(String key) {
        String value = getString(key);
        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    default Integer getInt(String key, Integer defaultVal) {
        return Null.of(getInt(key), defaultVal);
    }

    default Boolean getBoolean(String key) {
        String value = getString(key);
        if (value == null) {
            return null;
        }

        return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true");
    }

    default Boolean getBoolean(String key, boolean defaultVal) {
        return Null.of(getBoolean(key), defaultVal);
    }

    default Double getDouble(String key) {
        String value = getString(key);
        if (value == null) {
            return null;
        }

        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return null;
        }
    }

    default Double getDouble(String key, Double defaultVal) {
        return Null.of(getDouble(key), defaultVal);
    }

    default Long getLong(String key) {
        String value = getString(key);
        if (value == null) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return null;
        }
    }

    default Long getLong(String key, Long defaultVal) {
        return Null.of(getLong(key), defaultVal);
    }
}
