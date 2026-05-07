package com.github.mawen12.agentx.core.plugins.redis.interceptor.metric;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.metric.ServiceMetricRegistry;
import com.github.mawen12.agentx.api.metric.Tags;
import com.github.mawen12.agentx.api.utils.ContextUtils;

public abstract class BaseMetricInterceptor implements NonReentrantInterceptor {
    private static final Logger LOGGER = Agent.getLogger(BaseMetricInterceptor.class);

    private RedisMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "cache-redis", "signature");
        metric = ServiceMetricRegistry.getOrCreate(tags, RedisMetric.REDIS_NAME_FACTORY, RedisMetric::new);
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        String key = getKey(methodInfo, ctx);
        metric.collect(key, ContextUtils.getDuration(ctx), methodInfo.isSuccess());
    }

    public abstract String getKey(MethodInfo methodInfo, Context ctx);

    @Override
    public Signal signal() {
        return Signal.METRIC;
    }
}
