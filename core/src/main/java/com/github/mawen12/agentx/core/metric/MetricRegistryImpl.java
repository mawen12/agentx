package com.github.mawen12.agentx.core.metric;

import com.github.mawen12.agentx.api.metric.*;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MetricRegistryImpl implements MetricRegistry {

    private final com.codahale.metrics.MetricRegistry metricRegistry;
    private final Map<String, Metric> metricCache = new HashMap<>();

    public static MetricRegistry build(com.codahale.metrics.MetricRegistry metricRegistry) {
        return metricRegistry == null ? MetricRegistry.NOOP : new MetricRegistryImpl(metricRegistry);
    }

    @Override
    public Counter counter(String name) {
        return getOrCreate(name, () -> CounterImpl.build(metricRegistry.counter(name)));
    }

    @Override
    public Timer timer(String name) {
        return getOrCreate(name, () -> TimerImpl.build(metricRegistry.timer(name)));
    }

    @Override
    public Histogram histogram(String name) {
        return getOrCreate(name, () -> HistogramImpl.build(metricRegistry.histogram(name)));
    }

    @Override
    public Meter meter(String name) {
        return getOrCreate(name, () -> MeterImpl.build(metricRegistry.meter(name)));
    }

    @Override
    public <T> Gauge<T> gauge(String name, MetricSupplier<Gauge<T>> supplier) {
        return getOrCreate(name, () -> metricRegistry.gauge(name, () -> GaugeImpl.build(supplier.newMetric())).getGauge());
    }

    private <T extends Metric> T getOrCreate(String name, MetricSupplier<T> supplier) {
        Metric metric = metricCache.get(name);
        if (metric != null) {
            return (T) metric;
        }

        synchronized (metricCache) {
            metric = metricCache.get(name);
            if (metric != null) {
                return (T) metric;
            }

            metric = supplier.newMetric();
            metricCache.put(name, metric);
        }
        return (T) metric;
    }
}
