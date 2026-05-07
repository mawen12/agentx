package com.github.mawen12.agentx.core.plugins.logback.interceptor.metric;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.metric.ServiceMetricRegistry;
import com.github.mawen12.agentx.api.metric.Tags;
import com.github.mawen12.agentx.core.plugins.log4j2.interceptor.metric.Log4j2Metric;

public class LogbackMetricInterceptor implements NonReentrantInterceptor {
    public static final LogbackMetricInterceptor INSTANCE = new LogbackMetricInterceptor();
    private static final Logger LOGGER = Agent.getLogger(LogbackMetricInterceptor.class);

    private LogbackMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "logback-events", "level");
        metric = ServiceMetricRegistry.getOrCreate(tags, LogbackMetric.LOGBACK_NAME_FACTORY, LogbackMetric::new);
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
