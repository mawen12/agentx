package com.github.mawen12.agentx.api.interceptor;

import com.github.mawen12.agentx.api.context.Context;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface Interceptor {
    Signal signal();

    default int order() {
        return signal().getOrder();
    }

    default void init() {
    }

    void before(MethodInfo methodInfo, Context ctx);

    void after(MethodInfo methodInfo, Context ctx);

    @Getter
    @AllArgsConstructor
    enum Signal {
        PREPARE("prepare", 20),
        TRACING("tracing", 100),
        METRIC("metric", 200),
        LOGGING("logging", 201);

        private final String name;
        private final int order;
    }
}
