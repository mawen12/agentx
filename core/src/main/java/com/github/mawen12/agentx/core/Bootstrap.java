package com.github.mawen12.agentx.core;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.metric.MetricRegistryManager;
import com.github.mawen12.agentx.core.config.ConfigFactory;
import com.github.mawen12.agentx.core.context.ContextManagerImpl;
import com.github.mawen12.agentx.core.logging.AgentLogger;
import com.github.mawen12.agentx.core.logging.AgentLoggerFactory;
import com.github.mawen12.agentx.core.metric.MetricRegistryManagerImpl;
import com.github.mawen12.agentx.core.utils.NetUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.lang.instrument.Instrumentation;

public class Bootstrap {
    private static final Logger LOGGER = new AgentLogger(LogManager.getLogger(Bootstrap.class));

    public static ClassLoader LOADER;

    public static void premain(String args, Instrumentation inst, String jarPath) throws Exception {
        long begin = System.nanoTime();
        Bootstrap.LOADER = Thread.currentThread().getContextClassLoader();
        Agent.config = ConfigFactory.loadConfig(jarPath, Bootstrap.LOADER);

        initLoggerLevel();

        LOGGER.info("agent premain start, jarPath: {}", jarPath);

        Agent.loggerFactory = new AgentLoggerFactory();
        if (Agent.config != null) {
            Agent.additionalAttributes.put("service", Agent.config.getString("name"));
            Agent.additionalAttributes.put("system", Agent.config.getString("system"));
            Agent.additionalAttributes.put("host_ipv4", NetUtils.getHostIpV4());
            Agent.additionalAttributes.put("host_name", NetUtils.getHostName());
        }

        Agent.contextManager = ContextManagerImpl.build();

        Agent.metricRegistryManager = MetricRegistryManagerImpl.build();

        // todo
    }

    private static void initLoggerLevel() {
        String level = Agent.config.getString("logging.level");
        Level loggerLevel = Level.INFO;
        switch (level) {
            case "OFF":
            case "off":
                loggerLevel = Level.OFF;
                break;
            case "FATAL":
            case "fatal":
                loggerLevel = Level.FATAL;
                break;
            case "ERROR":
            case "error":
                loggerLevel = Level.ERROR;
                break;
            case "DEBUG":
            case "debug":
                loggerLevel = Level.DEBUG;
                break;
            case "TRACE":
            case "trace":
                loggerLevel = Level.TRACE;
                break;
        }

        Configurator.setLevel("com.github.mawen12.agentx", loggerLevel);
    }
}
