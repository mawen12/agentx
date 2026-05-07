package com.github.mawen12.agentx.core.plugins.logback.interceptor.metric;

import com.github.mawen12.agentx.api.metric.MetricRegistry;
import com.github.mawen12.agentx.api.metric.NameFactory;
import com.github.mawen12.agentx.api.metric.ServiceMetric;

import static com.github.mawen12.agentx.api.metric.Metric.Field.EXECUTION_COUNT;
import static com.github.mawen12.agentx.api.metric.Metric.FieldWrapper.of;
import static com.github.mawen12.agentx.api.metric.Metric.SubType.DEFAULT;
import static com.github.mawen12.agentx.api.metric.Metric.ValueFetcher.CountingCount;

public class LogbackMetric extends ServiceMetric {
    public static final NameFactory LOGBACK_NAME_FACTORY = NameFactorySupplier.INSTANCE.nameFactory();

    public LogbackMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public void collect(String key) {
        counter(key, DEFAULT).inc();
    }

    enum NameFactorySupplier implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createBuilder()
                    .counter(DEFAULT,
                            of(EXECUTION_COUNT, CountingCount)
                    )
                    .build();
        }
    }
}
