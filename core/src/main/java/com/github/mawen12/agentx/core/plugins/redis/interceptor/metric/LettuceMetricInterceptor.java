package com.github.mawen12.agentx.core.plugins.redis.interceptor.metric;

import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import io.lettuce.core.protocol.RedisCommand;

import java.util.Collection;
import java.util.stream.Collectors;

public class LettuceMetricInterceptor extends BaseMetricInterceptor {
    public static final LettuceMetricInterceptor INSTANCE = new LettuceMetricInterceptor();

    @Override
    public String getKey(MethodInfo methodInfo, Context ctx) {
        return cmd(methodInfo.getArgs()[0]);
    }

    private String cmd(Object arg0) {
        String cmd = null;
        if (arg0 instanceof RedisCommand) {
            RedisCommand<?, ?, ?> redisCommand = (RedisCommand<?, ?, ?>) arg0;
            cmd = redisCommand.getType().name();
        } else if (arg0 instanceof Collection) {
            Collection<RedisCommand<?, ?, ?>> redisCommands = (Collection<RedisCommand<?, ?, ?>>) arg0;
            cmd = "[" + String.join(",", redisCommands.stream().map(input -> input.getType().name()).collect(Collectors.toList()) + "]");
        }

        return cmd;
    }
}
