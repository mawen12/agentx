package com.github.mawen12.agent.api.metric;

public interface Gauge<T> extends Metric {
    Gauge NOOP = new NoOp();

    T getValue();

    class NoOp<T> implements Gauge<T> {
        @Override
        public T getValue() {
            return null;
        }
    }
}
