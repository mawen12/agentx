package com.github.mawen12.agentx.core.plugins.druid.metric;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.metric.*;
import com.github.mawen12.agentx.api.utils.ScheduleHelper;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.github.mawen12.agentx.api.metric.Metric.SubType.DEFAULT;

public class DruidDataSourceMetric extends ServiceMetric implements Runnable {
    private static final Logger LOGGER = Agent.getLogger(DruidDataSourceMetric.class);

    private MBeanServer mbs;
    private Set<ObjectName> names;

    public DruidDataSourceMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public static void buildAndRun() {
        Tags tags = new Tags("app", "druid-pool", "resource");
        DruidDataSourceMetric metric = ServiceMetricRegistry.getOrCreate(tags, DruidNameFactory.INSTANCE.nameFactory(), DruidDataSourceMetric::new);
        metric.init();

        if (metric.names != null && !metric.names.isEmpty()) {
            ScheduleHelper.INSTANCE.execute(5, 5, metric);
        }
    }

    private void init() {
        this.mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            Set<ObjectName> names = mbs.queryNames(new ObjectName("com.alibaba.druid:type=DruidDataSource,*"), null);
            this.names = names;
            LOGGER.info("Druid Metric name is {}", this.names);
        } catch (MalformedObjectNameException e) {
            LOGGER.warn("Druid Metric init failed", e);
        }
    }

    @Override
    public void run() {
        for (ObjectName name : names) {
            io.dropwizard.metrics5.MetricName datasourceName = io.dropwizard.metrics5.MetricRegistry.name("datasources", name.getCanonicalName());
            Map<Metric.SubType, MetricName> map = nameFactory.gaugeNames(datasourceName.getKey());
            for (Map.Entry<Metric.SubType, MetricName> entry : map.entrySet()) {
                MetricName metricName = entry.getValue();

                try {
                    Integer activeCount = getInt(name, "ActiveCount");
                    Integer poolingCount = getInt(name, "PoolingCount");
                    Integer maxActive = getInt(name, "MaxActive");
                    Integer waitThreadCount = getInt(name, "WaitThreadCount");
                    Gauge<DruidDataSourceGaugeMetricModel> gauge = () -> new DruidDataSourceGaugeMetricModel(
                            activeCount,
                            poolingCount,
                            maxActive,
                            waitThreadCount
                    );

                    metricRegistry.gauge(metricName.getName(), () -> gauge);
                } catch (Exception e) {
                    LOGGER.warn("Druid metric fetch failed", e);
                }
            }
        }
    }

    private Integer getInt(ObjectName name, String attribute) throws Exception {
        return (Integer) mbs.getAttribute(name, attribute);
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
