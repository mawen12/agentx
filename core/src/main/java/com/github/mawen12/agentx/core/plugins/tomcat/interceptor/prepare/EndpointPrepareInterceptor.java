package com.github.mawen12.agentx.core.plugins.tomcat.interceptor.prepare;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.metric.ServiceMetricRegistry;
import com.github.mawen12.agentx.api.metric.Tags;
import com.github.mawen12.agentx.core.plugins.tomcat.interceptor.metric.TomcatMetric;
import org.apache.tomcat.util.net.AbstractEndpoint;

public class EndpointPrepareInterceptor implements NonReentrantInterceptor {
    public static final EndpointPrepareInterceptor INSTANCE = new EndpointPrepareInterceptor();

    private static final Logger LOGGER = Agent.getLogger(EndpointPrepareInterceptor.class);

    private TomcatMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "tomcat", "resource");
        metric = ServiceMetricRegistry.getOrCreate(tags, TomcatMetric.TOMCAT_NAME_FACTORY, TomcatMetric::new);
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        if (methodInfo.getInvoker() instanceof AbstractEndpoint) {
            AbstractEndpoint<?, ?> endpoint = (AbstractEndpoint<?, ?>) methodInfo.getInvoker();
            metric.registerAndRun(endpoint);
        } else {
            LOGGER.warn("{} is not AbstractEndpoint", methodInfo.getInvoker().getClass().getName());
        }
    }

    @Override
    public Order order() {
        return Order.PREPARE;
    }
}
