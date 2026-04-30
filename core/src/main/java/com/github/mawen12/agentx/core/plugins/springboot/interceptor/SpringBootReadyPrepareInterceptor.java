package com.github.mawen12.agentx.core.plugins.springboot.interceptor;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.logging.Logger;

public enum SpringBootReadyPrepareInterceptor implements NonReentrantInterceptor {
    INSTANCE;

    private static final Logger LOGGER = Agent.getLogger(SpringBootReadyPrepareInterceptor.class);

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        Object event = methodInfo.getArgs()[0];
        if ("org.springframework.boot.context.event.ApplicationReadyEvent".equals(event.getClass().getCanonicalName())) {
            LOGGER.info("SpringBoot is ready, mark SpringBootReady state");
            Agent.markSpringBootReady();
        }
    }


    @Override
    public Signal signal() {
        return Signal.PREPARE;
    }
}
