package com.github.mawen12.agentx.core.plugins.logback.interceptor.prepare;

import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.log.LogConverter;
import com.github.mawen12.agentx.core.plugins.logback.common.LogbackConverter;

public enum LogbackPrepareInterceptor implements NonReentrantInterceptor {
    INSTANCE(LogbackConverter.INSTANCE);

    private final LogConverter logbackConverter;

    LogbackPrepareInterceptor(LogConverter logbackConverter) {
        this.logbackConverter = logbackConverter;
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        AppLogData data = logbackConverter.convert(methodInfo);
        if (data != null) {
            ctx.put(AppLogData.class, data);
        }
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        // NOP
    }


    @Override
    public Signal signal() {
        return Signal.PREPARE;
    }
}
