package com.github.mawen12.agentx.core.plugins.log4j2.interceptor.log;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.log.AppLogData;

public enum Log4j2LogInterceptor implements NonReentrantInterceptor {
    INSTANCE;

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        AppLogData data = ctx.get(AppLogData.class);
        if (data != null) {
            Agent.getReporter().report(data);
        }
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public Signal signal() {
        return Signal.LOGGING;
    }
}
