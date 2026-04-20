package com.github.mawen12.agent.api.metric;

import com.github.mawen12.agent.api.Agent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

public class ServiceMetricRegistry {

    public static final ConcurrentMap<Tags, ServiceMetric> INSTANCES = new ConcurrentHashMap<>();

    public static <T extends ServiceMetric> T getOrCreate(Tags tags, NameFactory nameFactory, BiFunction<MetricRegistry, NameFactory, T> supplier) {
        ServiceMetric metric = INSTANCES.get(tags);
        if (metric != null) {
            return (T) metric;
        }

        synchronized (INSTANCES) {
            metric = INSTANCES.get(tags);
            if (metric != null) {
                return (T) metric;
            }

            metric = supplier.apply(Agent.newMetricRegistry(tags, nameFactory), nameFactory);
            INSTANCES.put(tags, metric);
        }

        return (T) metric;
    }
}
