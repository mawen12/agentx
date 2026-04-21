package com.github.mawen12.agentx.core.metric;

import com.github.mawen12.agentx.api.metric.Meter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeterImpl implements Meter {

    private final io.dropwizard.metrics5.Meter meter;

    public static Meter build(io.dropwizard.metrics5.Meter meter) {
        return meter == null ? Meter.NOOP : new MeterImpl(meter);
    }

    @Override
    public void mark() {
        meter.mark();
    }

    @Override
    public double getOneMinuteRate() {
        return meter.getOneMinuteRate();
    }

    @Override
    public double getFiveMinuteRate() {
        return meter.getFiveMinuteRate();
    }

    @Override
    public double getFifteenMinuteRate() {
        return meter.getFifteenMinuteRate();
    }

    @Override
    public double getMeanRate() {
        return meter.getMeanRate();
    }

    @Override
    public long getCount() {
        return meter.getCount();
    }
}
