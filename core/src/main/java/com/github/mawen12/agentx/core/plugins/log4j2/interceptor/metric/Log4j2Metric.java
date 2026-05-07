package com.github.mawen12.agentx.core.plugins.log4j2.interceptor.metric;

import com.github.mawen12.agentx.api.metric.MetricRegistry;
import com.github.mawen12.agentx.api.metric.NameFactory;
import com.github.mawen12.agentx.api.metric.ServiceMetric;

import static com.github.mawen12.agentx.api.metric.Metric.Field.EXECUTION_COUNT;
import static com.github.mawen12.agentx.api.metric.Metric.FieldWrapper.of;
import static com.github.mawen12.agentx.api.metric.Metric.SubType.DEFAULT;
import static com.github.mawen12.agentx.api.metric.Metric.ValueFetcher.CountingCount;

public class Log4j2Metric extends ServiceMetric {
    public static final NameFactory LOG4J2_NAME_FACTORY = NameFactorySupplier.INSTANCE.nameFactory();

    public Log4j2Metric(MetricRegistry metricRegistry, NameFactory nameFactory) {
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
