package com.github.mawen12.agentx.core.metric;

import com.github.mawen12.agentx.api.metric.Histogram;

public class HistogramImpl extends AbstractSnapshot implements Histogram {

    private final io.dropwizard.metrics5.Histogram histogram;

    private HistogramImpl(io.dropwizard.metrics5.Histogram histogram) {
        super(histogram.getSnapshot());
        this.histogram = histogram;
    }

    public static Histogram build(io.dropwizard.metrics5.Histogram histogram) {
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
