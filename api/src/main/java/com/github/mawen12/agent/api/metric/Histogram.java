package com.github.mawen12.agent.api.metric;

public interface Histogram extends Snapshot, Metric{
    Histogram NOOP = NoOp.INSTANCE;

    void update(long value);

    long getCount();

    enum NoOp implements Histogram {
        INSTANCE;

        @Override
        public void update(long value) {

        }

        @Override
        public long getCount() {
            return 0;
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
            return INSTANCE;
        }
    }
}
