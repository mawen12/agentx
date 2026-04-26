package com.github.mawen12.agentx.core.plugins.jdbc.common;

import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.metric.*;
import com.github.mawen12.agentx.api.utils.ContextUtils;
import com.github.mawen12.agentx.core.metric.LastMinutesCounterGauge;

import static com.github.mawen12.agentx.api.metric.Metric.SubType.DEFAULT;
import static com.github.mawen12.agentx.api.metric.Metric.SubType.ERROR;

public class JdbcMetric extends ServiceMetric {
    public static NameFactory JDBC_NAME_FACTORY = JdbcNameFactory.INSTANCE.nameFactory();

    public JdbcMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public void collectMetric(String key, boolean success, Context ctx) {
        timer(key, DEFAULT).updateMillis(ContextUtils.getDuration(ctx));
        counter(key, DEFAULT).inc();
        Meter meter = meter(key, DEFAULT);
        meter.mark();

        if (!success) {
            counter(key, ERROR).inc();
            meter(key, ERROR).mark();
        }

        gauge(key, DEFAULT, () -> () ->
                new LastMinutesCounterGauge(
                        (long) meter.getOneMinuteRate() * 60,
                        (long) meter.getFiveMinuteRate() * 60 * 5,
                        (long) meter.getFifteenMinuteRate() * 60 * 15)
        );
    }

    enum JdbcNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createDefault();
        }
    }
}
