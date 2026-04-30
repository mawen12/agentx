package com.github.mawen12.agentx.core.plugins.druid.interceptor.prepare;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.metric.ServiceMetricRegistry;
import com.github.mawen12.agentx.api.metric.Tags;
import com.github.mawen12.agentx.core.plugins.druid.interceptor.metric.DruidMetric;

public class DruidDataSourcePreapreInterceptor implements Interceptor {
    public static DruidDataSourcePreapreInterceptor INSTANCE = new DruidDataSourcePreapreInterceptor();

    private static final Logger LOGGER = Agent.getLogger(DruidDataSourcePreapreInterceptor.class);

    private DruidMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "druid-pool", "resource");
        metric = ServiceMetricRegistry.getOrCreate(tags, DruidMetric.DRUID_NAME_FACTORY, DruidMetric::new);
    }

    @Override
    public void before(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void after(MethodInfo methodInfo, Context ctx) {
        if (methodInfo.getInvoker() instanceof DruidDataSource) {
            DruidDataSource newInstance = (DruidDataSource) methodInfo.getInvoker();
            metric.registerAndRun(newInstance);
        } else {
            LOGGER.warn("{} is not DruidDataSource", methodInfo.getInvoker().getClass().getName());
        }
    }

    @Override
    public Order order() {
        return Order.PREPARE;
    }
}
