package com.github.mawen12.agentx.core.metric;

import com.github.mawen12.agentx.api.metric.MetricRegistry;
import com.github.mawen12.agentx.api.metric.MetricRegistryManager;
import com.github.mawen12.agentx.api.metric.NameFactory;
import com.github.mawen12.agentx.api.metric.Tags;
import com.github.mawen12.agentx.core.metric.prometheus.AgentPrometheusExports;
import com.github.mawen12.agentx.core.metric.prometheus.AgentSampleBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.github.mawen12.agentx.api.Agent.additionalAttributes;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MetricRegistryManagerImpl implements MetricRegistryManager {

    private final MetricRegistry metricRegistry;

    public static MetricRegistryManager build() {
        io.dropwizard.metrics5.MetricRegistry mr = new io.dropwizard.metrics5.MetricRegistry();
        return new MetricRegistryManagerImpl(MetricRegistryImpl.build(mr));
    }

    @Override
    public MetricRegistry newMetricRegistry(Tags tags, NameFactory nameFactory) {
        io.dropwizard.metrics5.MetricRegistry mr = new io.dropwizard.metrics5.MetricRegistry();

        AgentSampleBuilder sampleBuilder = new AgentSampleBuilder(additionalAttributes, tags);
        AgentPrometheusExports agentPrometheusExports = new AgentPrometheusExports(mr, nameFactory, sampleBuilder);
        agentPrometheusExports.register();

        return MetricRegistryImpl.build(mr);
    }

}
