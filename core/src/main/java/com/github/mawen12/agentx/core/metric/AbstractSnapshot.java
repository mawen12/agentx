package com.github.mawen12.agentx.core.metric;

import com.github.mawen12.agentx.api.metric.Snapshot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractSnapshot implements Snapshot {

    private final com.codahale.metrics.Snapshot snapshot;

    @Override
    public double getValue(double quantile) {
        return snapshot.getValue(quantile);
    }

    @Override
    public long[] getValues() {
        return snapshot.getValues();
    }

    @Override
    public long getMax() {
        return snapshot.getMax();
    }

    @Override
    public long getMin() {
        return snapshot.getMin();
    }

    @Override
    public double getMean() {
        return snapshot.getMean();
    }

    @Override
    public Snapshot unwrap() {
        return this;
    }
}
