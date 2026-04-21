package com.github.mawen12.agentx.api.metric;

public interface MetricRegistryManager {

    MetricRegistry newMetricRegistry(Tags tags, NameFactory nameFactory);
}
