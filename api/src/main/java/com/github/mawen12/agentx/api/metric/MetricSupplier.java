package com.github.mawen12.agentx.api.metric;

public interface MetricSupplier<M extends Metric> {

    M newMetric();
}
