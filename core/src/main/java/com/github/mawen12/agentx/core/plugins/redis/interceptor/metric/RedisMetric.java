package com.github.mawen12.agentx.core.plugins.redis.interceptor.metric;

import com.github.mawen12.agentx.api.metric.Meter;
import com.github.mawen12.agentx.api.metric.MetricRegistry;
import com.github.mawen12.agentx.api.metric.NameFactory;
import com.github.mawen12.agentx.api.metric.ServiceMetric;
import com.github.mawen12.agentx.core.metric.LastMinutesCounterGauge;

import static com.github.mawen12.agentx.api.metric.Metric.SubType.DEFAULT;
import static com.github.mawen12.agentx.api.metric.Metric.SubType.ERROR;

public class RedisMetric extends ServiceMetric {
    public static final NameFactory REDIS_NAME_FACTORY = NameFactorySupplier.INSTANCE.nameFactory();

    public RedisMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public void collect(String key, long duration, boolean success) {
        timer(key, DEFAULT).updateMillis(duration);
        counter(key, DEFAULT).inc();
        Meter meter = meter(key, DEFAULT);
        meter.mark();

        if (!success) {
            counter(key, ERROR).inc();
            meter(key, ERROR).mark();
        }

        gauge(key, DEFAULT, () -> () -> new LastMinutesCounterGauge(
                (long) meter.getOneMinuteRate() * 60,
                (long) meter.getFiveMinuteRate() * 60 * 5,
                (long) meter.getFifteenMinuteRate() * 60 * 15));
    }

    public enum NameFactorySupplier implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createDefault();
        }
    }
}
