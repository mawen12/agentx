package com.github.mawen12.agentx.core.metric;

import com.github.mawen12.agentx.api.metric.Histogram;

public class HistogramImpl extends AbstractSnapshot implements Histogram {

    private final com.codahale.metrics.Histogram histogram;

    private HistogramImpl(com.codahale.metrics.Histogram histogram) {
        super(histogram.getSnapshot());
        this.histogram = histogram;
    }

    public static Histogram build(com.codahale.metrics.Histogram histogram) {
        return histogram == null ? Histogram.NOOP : new HistogramImpl(histogram);
    }

    @Override
    public void update(long value) {
        histogram.update(value);
    }

    @Override
    public long getCount() {
        return histogram.getCount();
    }
}
