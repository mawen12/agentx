package com.github.mawen12.agentx.api;

import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.metric.MetricRegistry;
import com.github.mawen12.agentx.api.metric.NameFactory;
import com.github.mawen12.agentx.api.metric.Tags;

public class Agent {

    public static MetricRegistry newMetricRegistry(Tags tags, NameFactory nameFactory) {
        // TODO
    }

    public static Logger getLogger(String name) {
        // TODO
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getCanonicalName());
    }
}
