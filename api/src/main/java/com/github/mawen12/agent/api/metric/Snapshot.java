package com.github.mawen12.agent.api.metric;

public interface Snapshot {
    Snapshot NOOP = NoOp.INSTANCE;

    double getValue(double quantile);

    long[] getValues();

    long getMax();

    long getMin();

    double getMean();

    default double getMedian() {
        return getValue(0.5);
    }

    default double get25thPercentile() {
        return getValue(0.25);
    }

    default double get75thPercentile() {
        return getValue(0.75);
    }

    default double get98thPercentile() {
        return getValue(0.98);
    }

    default double get99thPercentile() {
        return getValue(0.99);
    }

    default double get999thPercentile() {
        return getValue(0.999);
    }

    Snapshot unwrap();

    enum NoOp implements Snapshot {
        INSTANCE;

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
