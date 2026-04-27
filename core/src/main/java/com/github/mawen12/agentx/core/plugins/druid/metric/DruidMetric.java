package com.github.mawen12.agentx.core.plugins.druid.metric;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.metric.MetricRegistry;
import com.github.mawen12.agentx.api.metric.NameFactory;
import com.github.mawen12.agentx.api.metric.ServiceMetric;
import com.github.mawen12.agentx.api.utils.ScheduleHelper;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.github.mawen12.agentx.api.metric.Metric.SubType.DEFAULT;

public class DruidMetric extends ServiceMetric implements Runnable {
    public static final NameFactory DRUID_NAME_FACTORY = DruidNameFactory.INSTANCE.nameFactory();

    private static final Logger LOGGER = Agent.getLogger(DruidMetric.class);

    private final ConcurrentMap<String, WeakReference<DruidDataSource>> instances = new ConcurrentHashMap<>();

    private volatile boolean started;

    public DruidMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public void registerAndRun(DruidDataSource druidDataSource) {
        String key = Integer.toHexString(System.identityHashCode(this));
        if (!instances.containsKey(key)) {
            LOGGER.info("register new instance for {}", key);
            instances.put(key, new WeakReference<>(druidDataSource));

            if (!started) {
                synchronized (this) {
                    if (!started) {
                        ScheduleHelper.INSTANCE.execute(5, 5, this);
                        started = true;
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        for (Map.Entry<String, WeakReference<DruidDataSource>> entry : instances.entrySet()) {
            WeakReference<DruidDataSource> value = entry.getValue();
            DruidDataSource druidDataSource = value.get();
            if (druidDataSource != null && druidDataSource.isInited()) {
                int activeCount = druidDataSource.getActiveCount();
                int poolingCount = druidDataSource.getPoolingCount();
                int maxActive = druidDataSource.getMaxActive();
                int waitThreadCount = druidDataSource.getWaitThreadCount();

                gauge(druidDataSource.getName(), DEFAULT, () -> () -> new DruidDataSourceGaugeMetricModel(
                        activeCount,
                        poolingCount,
                        maxActive,
                        waitThreadCount
                ));
            }
        }
    }

    enum DruidNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createBuilder()
                    .gauge(DEFAULT, Collections.emptySet())
                    .build();
        }
    }
}
