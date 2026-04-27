package com.github.mawen12.agentx.core.metric;

import com.github.mawen12.agentx.api.metric.Counter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CounterImpl implements Counter {

    private com.codahale.metrics.Counter counter;

    public static Counter build(com.codahale.metrics.Counter counter) {
        return counter == null ? Counter.NOOP : new CounterImpl(counter);
    }

    @Override
    public void inc() {
        counter.inc();
    }

    @Override
    public void inc(long n) {
        counter.inc(n);
    }

    @Override
    public long count() {
        return counter.getCount();
    }
}
