package com.github.mawen12.agentx.core.config;

import com.github.mawen12.agentx.api.config.Config;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.logging.LoggerFactory;
import com.github.mawen12.agentx.core.Bootstrap;
import com.github.mawen12.agentx.core.logging.AgentLogger;
import com.github.mawen12.agentx.core.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ConfigFactory {
    private static final Logger LOGGER = new AgentLogger(LogManager.getLogger(ConfigFactory.class));

    public static final String CONFIG_PATH_PROPERTY = "agent.config.path";
    public static final String CONFIG_PATH_ENV = "AGENT_CONFIG_PATH";
    public static final String DEFAULT_CONFIG_PATH = "agent.properties";
    public static final Map<String, String> CUSTOM_CONFIG_KEYS = ImmutableMap.<String, String>builder()
            .put("AGENT_NAME", "agent.name")
            .put("AGENT_SYSTEM", "agent.system")
            .put("AGENT_SERVER_PORT", "agent.server.port")
            .put("AGENT_SERVER_ENABLED", "agent.server.enabled")
            .put("AGENT_LOGGING_LEVEL", "agent.logging.level")
            .build();

    public static Config loadConfig(String jarPath) {
        ConfigImpl defaultConfig = loadDefaultConfig(jarPath, DEFAULT_CONFIG_PATH);

        String customConfigPath = getConfigPath();
        if (!StringUtils.isEmpty(customConfigPath)) {
            Config customConfig = loadFromFile(new File(customConfigPath));
            defaultConfig.mergeConfigs(customConfig);
        }

        Config envConfig = loadEnvConfig();
        defaultConfig.mergeConfigs(envConfig);

        return defaultConfig;
    }

    private static ConfigImpl loadDefaultConfig(String jarPath, String configFileName) {
        try (JarFile jarFile = new JarFile(new File(jarPath))) {
            ZipEntry zipEntry = jarFile.getEntry(configFileName);
            if (zipEntry == null) {
                return null;
            }

            try (InputStream in = jarFile.getInputStream(zipEntry)) {
                return loadFromStream(in);
            }
        } catch (Exception e) {
        }
        return null;
    }

    private static String getConfigPath() {
        String value = System.getProperty(CONFIG_PATH_PROPERTY);
        if (value != null) {
            return value;
        }

        return System.getenv(CONFIG_PATH_ENV);
    }

    private static ConfigImpl loadFromStream(InputStream in) throws IOException {
        if (in != null) {
            Properties properties = new Properties();
            properties.load(in);

            Map<String, String> map = new HashMap<>();
            for (String key : properties.stringPropertyNames()) {
                map.put(key, properties.getProperty(key));
            }
            return new ConfigImpl(map);
        }

        return new ConfigImpl(Collections.emptyMap());
    }

    private static Config loadFromFile(File configFile) {
        try (FileInputStream in = new FileInputStream(configFile)) {
            return loadFromStream(in);
        } catch (IOException e) {
            LOGGER.warn("Load config file failure: {}", configFile.getAbsolutePath(), e);
        }

        return new ConfigImpl(Collections.emptyMap());
    }

    private static Config loadEnvConfig() {
        Map<String, String> envConfig = new HashMap<>();

        for (Map.Entry<String, String> entry : CUSTOM_CONFIG_KEYS.entrySet()) {
            String configKey = entry.getValue().replaceFirst("agent.", "");
            String value = System.getenv(entry.getKey());
            if (!StringUtils.isEmpty(value)) {
                envConfig.put(configKey, value);
            }

            value = System.getProperty(entry.getValue());
            if (!StringUtils.isEmpty(value)) {
                envConfig.put(configKey, value);
            }
        }

        return new ConfigImpl(envConfig);
    }
}
