package com.github.mawen12.agentx.core.plugins.redis.interceptor.metric;

import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;

public class JedisMetricInterceptor extends BaseMetricInterceptor {
    public static final JedisMetricInterceptor INSTANCE = new JedisMetricInterceptor();

    @Override
    public String getKey(MethodInfo methodInfo, Context ctx) {
        return methodInfo.getMethod();
    }
}
