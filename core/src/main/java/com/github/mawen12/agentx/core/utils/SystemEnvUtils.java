package com.github.mawen12.agentx.core.utils;

public class SystemEnvUtils {

    public static String toEnvVarName(String propertyName) {
        return propertyName.toUpperCase().replace('-', '_').replace('.', '_');
    }
}
