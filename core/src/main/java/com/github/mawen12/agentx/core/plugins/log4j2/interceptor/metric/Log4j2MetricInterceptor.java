package com.github.mawen12.agentx.core.plugins.log4j2.interceptor.metric;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.metric.ServiceMetricRegistry;
import com.github.mawen12.agentx.api.metric.Tags;

public class Log4j2MetricInterceptor implements NonReentrantInterceptor {
    public static final Log4j2MetricInterceptor INSTANCE = new Log4j2MetricInterceptor();
    private static final Logger LOGGER = Agent.getLogger(Log4j2MetricInterceptor.class);

    private Log4j2Metric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "log4j2-events", "level");
        metric = ServiceMetricRegistry.getOrCreate(tags, Log4j2Metric.LOG4J2_NAME_FACTORY, Log4j2Metric::new);
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        AppLogData data = ctx.get(AppLogData.class);

        if (data != null) {
            metric.collect(data.getLevel());
        }
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public Signal signal() {
        return Signal.METRIC;
    }
}
