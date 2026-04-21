package com.github.mawen12.agentx.core.metric;

import com.github.mawen12.agentx.api.metric.Timer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TimerImpl extends AbstractSnapshot implements Timer {

    private final io.dropwizard.metrics5.Timer timer;

    public TimerImpl(io.dropwizard.metrics5.Timer timer) {
        super(timer.getSnapshot());
        this.timer = timer;
    }

    public static Timer build(io.dropwizard.metrics5.Timer timer) {
        return timer == null ? Timer.NOOP : new TimerImpl(timer);
    }

    @Override
    public void update(long duration, TimeUnit unit) {
        timer.update(duration, unit);
    }

    @Override
    public void update(Duration duration) {
        timer.update(duration);
    }
}
