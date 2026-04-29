package com.github.mawen12.agentx.core.plugins.logback.interceptor.log;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.log.LogConverter;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.core.plugins.logback.common.LogbackConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum LogbackLogInterceptor implements NonReentrantInterceptor {
    INSTANCE(LogbackConverter.INSTANCE);

    private static final Logger LOGGER = Agent.getLogger(LogbackLogInterceptor.class);

    private final LogConverter logbackConverter;

    LogbackLogInterceptor(LogConverter logbackConverter) {
        this.logbackConverter = logbackConverter;
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        AppLogData data = logbackConverter.convert(methodInfo);
        if (data != null) {
            LOGGER.info("app-log: Logger: {}, Thread: {}, Level: {}, Message: {}",
                    data.getLogger(), data.getThreadName(), data.getLevel(),
                    data.getMessage(), data.getThrowable());
        }
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public Order order() {
        return Order.LOG;
    }
}
