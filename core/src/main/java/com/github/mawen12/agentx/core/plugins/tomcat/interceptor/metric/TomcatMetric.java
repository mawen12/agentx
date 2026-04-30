package com.github.mawen12.agentx.core.plugins.tomcat.interceptor.metric;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.metric.Gauge;
import com.github.mawen12.agentx.api.metric.MetricRegistry;
import com.github.mawen12.agentx.api.metric.NameFactory;
import com.github.mawen12.agentx.api.metric.ServiceMetric;
import com.github.mawen12.agentx.api.utils.ScheduleHelper;
import org.apache.tomcat.util.net.AbstractEndpoint;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.github.mawen12.agentx.api.metric.Metric.SubType.DEFAULT;

public class TomcatMetric extends ServiceMetric implements Runnable {
    public static final NameFactory TOMCAT_NAME_FACTORY = TomcatNameFactory.INSTANCE.nameFactory();

    private static final Logger LOGGER = Agent.getLogger(TomcatMetric.class);

    private final ConcurrentMap<String, WeakReference<AbstractEndpoint<?, ?>>> instances = new ConcurrentHashMap<>();

    private volatile boolean started;

    public TomcatMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public void registerAndRun(AbstractEndpoint<?, ?> endpoint) {
        String key = endpoint.getName();

        if (!instances.containsKey(key)) {
            LOGGER.info("register tomcat endpoint instance for {}", key);
            instances.put(key, new WeakReference<>(endpoint));

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
        Iterator<Map.Entry<String, WeakReference<AbstractEndpoint<?, ?>>>> iter = instances.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, WeakReference<AbstractEndpoint<?, ?>>> entry = iter.next();
            WeakReference<AbstractEndpoint<?, ?>> value = entry.getValue();
            AbstractEndpoint<?, ?> endpoint = value.get();
            if (endpoint == null) {
                iter.remove();
            } else {

                int busy = endpoint.getCurrentThreadsBusy();
                int count = endpoint.getCurrentThreadCount();
                int max = endpoint.getMaxThreads();

                Gauge<TomcatGaugeMetricModel> gauge = () -> new TomcatGaugeMetricModel(
                        busy,
                        count,
                        max
                );

                gauge(endpoint.getName(), DEFAULT, () -> gauge);
            }
        }
    }

    enum TomcatNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createBuilder()
                    .gauge(DEFAULT, Collections.emptySet())
                    .build();
        }
    }
}
