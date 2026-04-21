package com.github.mawen12.agentx.api.interceptor;

import com.github.mawen12.agentx.api.context.Context;

public interface NonReentrantInterceptor extends Interceptor {

    @Override
    default void before(MethodInfo methodInfo, Context ctx) {
        Object key = getEnterKey(methodInfo, ctx);
        if (!ctx.enter(key, 1)) {
            return;
        }
        doBefore(methodInfo, ctx);
    }

    @Override
    default void after(MethodInfo methodInfo, Context ctx) {
        Object key = getEnterKey(methodInfo, ctx);
        if (!ctx.exit(key, 1)) {
            return;
        }

        try {
            ctx.enter(key);
            doAfter(methodInfo, ctx);
        } finally {
            ctx.exit(key);
        }
    }

    default Object getEnterKey(MethodInfo methodInfo, Context ctx) {
        return this.getClass();
    }

    void doBefore(MethodInfo methodInfo, Context ctx);

    void doAfter(MethodInfo methodInfo, Context ctx);
}
