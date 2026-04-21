package com.github.mawen12.agentx.api.metric;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public interface Timer extends Snapshot, Metric {
    Timer NOOP = NoOp.INSTANCE;

    void update(long duration, TimeUnit unit);

    default void updateMillis(long durationMillis) {
        this.update(durationMillis, TimeUnit.MILLISECONDS);
    }

    void update(Duration duration);

    enum NoOp implements Timer {
        INSTANCE;

        @Override
        public void update(long duration, TimeUnit unit) {

        }

        @Override
        public void update(Duration duration) {

        }

        @Override
        public double getValue(double quantile) {
            return 0;
        }

        @Override
        public long[] getValues() {
            return new long[0];
        }

        @Override
        public long getMax() {
            return 0;
        }

        @Override
        public long getMin() {
            return 0;
        }

        @Override
        public double getMean() {
            return 0;
        }

        @Override
        public Snapshot unwrap() {
            return null;
        }
    }
}
