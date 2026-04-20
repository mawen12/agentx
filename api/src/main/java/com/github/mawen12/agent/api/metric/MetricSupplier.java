package com.github.mawen12.agent.api.metric;

public interface MetricSupplier<M extends Metric> {

    M newMetric();
}
