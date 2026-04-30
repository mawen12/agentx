package com.github.mawen12.agentx.core;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.config.Constants;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.plugins.Plugin;
import com.github.mawen12.agentx.api.spi.BeanProvider;
import com.github.mawen12.agentx.core.agent.AgentIgnore;
import com.github.mawen12.agentx.core.agent.AgentListener;
import com.github.mawen12.agentx.core.agent.ClassTransformer;
import com.github.mawen12.agentx.core.config.ConfigFactory;
import com.github.mawen12.agentx.core.context.ContextManagerImpl;
import com.github.mawen12.agentx.core.logging.AgentLogger;
import com.github.mawen12.agentx.core.logging.AgentLoggerFactory;
import com.github.mawen12.agentx.core.metric.MetricRegistryManagerImpl;
import com.github.mawen12.agentx.core.metric.prometheus.MetricServer;
import com.github.mawen12.agentx.core.utils.NetUtils;
import com.github.mawen12.agentx.core.utils.ServiceLoaderUtils;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Bootstrap {
    private static final Logger LOGGER = new AgentLogger(LogManager.getLogger(Bootstrap.class));

    public static ClassLoader LOADER;

    public static void premain(String args, Instrumentation inst, String jarPath) throws Exception {
        long begin = System.nanoTime();
        Bootstrap.LOADER = Thread.currentThread().getContextClassLoader();
        Agent.agentClassLoader = Thread.currentThread().getContextClassLoader();

        initConfig(jarPath);

        LOGGER.info("config <{}> is {}", jarPath, Agent.config);

        initLogger();

        Agent.contextManager = ContextManagerImpl.build();

        Agent.metricRegistryManager = MetricRegistryManagerImpl.build();

        buildServer();

        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .with(AgentListener.INSTANCE)
                .ignore(AgentIgnore.ignored());

        List<BeanProvider> beanProviders = ServiceLoaderUtils.load(BeanProvider.class);
        for (BeanProvider beanProvider : beanProviders) {
            beanProvider.setConfig(Agent.config);
            Agent.addListener(beanProvider.onState(), beanProvider::afterPropertiesSet);
            LOGGER.info("register BeanProvider: {} on {}", beanProvider.getClass().getSimpleName(), beanProvider.onState().name());
        }

        Agent.markStart();

        List<Plugin> plugins = ServiceLoaderUtils.load(Plugin.class);
        for (Plugin plugin : plugins) {
            if (plugin instanceof ClassTransformer) {
                ClassTransformer transformer = (ClassTransformer) plugin;
                agentBuilder = transformer.build(agentBuilder);
                LOGGER.info("register ClassTransformer: {}", transformer.getClass().getSimpleName());
            }
            plugin.setConfig(Agent.config);
        }

        long installBegin = System.currentTimeMillis();
        agentBuilder.installOn(inst);

        LOGGER.info("installBegin use time: {}ms", (System.currentTimeMillis() - installBegin));
        LOGGER.info("initialization has took {}ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - begin));
    }

    private static void initConfig(String jarPath) {
        Agent.config = ConfigFactory.loadConfig(jarPath);

        Agent.additionalAttributes.put("service", Agent.config.getString(Constants.CONFIG_NAME));
        Agent.additionalAttributes.put("system", Agent.config.getString(Constants.CONFIG_SYSTEM));
        Agent.additionalAttributes.put("host_ipv4", NetUtils.getHostIpV4());
        Agent.additionalAttributes.put("host_name", NetUtils.getHostName());
    }

    private static void initLogger() {
        initLoggerLevel();
        initLoggerFactory();
    }

    private static void initLoggerLevel() {
        String level = Agent.config.getString(Constants.CONFIG_LOGGING_LEVEL);
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

    private static void initLoggerFactory() {
        Agent.loggerFactory = new AgentLoggerFactory();
    }

    private static void buildServer() throws IOException {
        if (Objects.equals(true, Agent.config.getBoolean(Constants.CONFIG_AGENT_SERVER_ENABLED))) {
            Integer port = Agent.config.getInt(Constants.CONFIG_AGENT_SERVER_PORT);
            new MetricServer(port).start();
        }
    }
}
