package com.github.mawen12.agent.api.metric;

public interface MetricRegistryManager {

    MetricRegistry getMetricRegistry();

    MetricRegistry newMetricRegistry(Tags tags, NameFactory nameFactory);
}
