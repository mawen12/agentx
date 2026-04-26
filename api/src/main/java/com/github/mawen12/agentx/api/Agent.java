package com.github.mawen12.agentx.api;

import com.github.mawen12.agentx.api.config.Config;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.context.ContextManager;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.logging.LoggerFactory;
import com.github.mawen12.agentx.api.metric.MetricRegistry;
import com.github.mawen12.agentx.api.metric.MetricRegistryManager;
import com.github.mawen12.agentx.api.metric.NameFactory;
import com.github.mawen12.agentx.api.metric.Tags;
import com.github.mawen12.agentx.api.spi.BeanProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agent {

    public static ContextManager contextManager = () -> Context.NOOP;
    public static LoggerFactory loggerFactory = LoggerFactory.NOOP;
    public static MetricRegistryManager metricRegistryManager = null;
    public static Config config;
    public static Map<BeanProvider.State, List<BeanProvider.Listener>> listeners = new HashMap<>();
    public static Map<String, Object> additionalAttributes = new HashMap<>();


    public static MetricRegistry newMetricRegistry(Tags tags, NameFactory nameFactory) {
        // TODO
    }

    public static Logger getLogger(String name) {
        // TODO
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getCanonicalName());
    }

    public static void markSpringBootReady() {
        notifyState(BeanProvider.State.SpringBootReady);
    }

    public static void notifyState(BeanProvider.State state) {
        List<BeanProvider.Listener> stateListeners = listeners.get(state);
        if (stateListeners != null) {
            for (BeanProvider.Listener listener : stateListeners) {
                listener.onState();
            }
        }
    }
}
