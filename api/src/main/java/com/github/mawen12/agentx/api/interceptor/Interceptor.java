package com.github.mawen12.agentx.api.interceptor;

import com.github.mawen12.agentx.api.context.Context;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface Interceptor {
    Order order();

    default void init() {}

    void before(MethodInfo methodInfo, Context ctx);

    void after(MethodInfo methodInfo, Context ctx);

    @Getter
    @AllArgsConstructor
    enum Order {
        PREPARE(20),
        TRACING(100),
        METRIC(200);

        private final int code;
    }
}
