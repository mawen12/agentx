package com.github.mawen12.agentx.api.metric;

public interface MetricRegistry {
    MetricRegistry NOOP = NoOp.INSTANCE;

    Counter counter(String name);

    Timer timer(String name);

    Histogram histogram(String name);

    Meter meter(String name);

    <T> Gauge<T> gauge(String name, MetricSupplier<Gauge<T>> supplier);

    enum NoOp implements MetricRegistry {
        INSTANCE;

        @Override
        public Counter counter(String name) {
            return Counter.NOOP;
        }

        @Override
        public Timer timer(String name) {
            return Timer.NOOP;
        }

        @Override
        public Histogram histogram(String name) {
            return Histogram.NOOP;
        }

        @Override
        public Meter meter(String name) {
            return Meter.NOOP;
        }

        @Override
        public <T> Gauge<T> gauge(String name, MetricSupplier<Gauge<T>> supplier) {
            return Gauge.NOOP;
        }
    }
}
