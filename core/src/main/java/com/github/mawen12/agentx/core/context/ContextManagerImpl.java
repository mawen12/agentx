package com.github.mawen12.agentx.core.context;

import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.context.ContextManager;

public class ContextManagerImpl implements ContextManager {

    private static final ThreadLocal<SessionContext> LOCAL_SESSION_CONTEXT = ThreadLocal.withInitial(SessionContext::new);

    public static ContextManager build() {
        return new ContextManagerImpl();
    }

    @Override
    public Context getContext() {
        return LOCAL_SESSION_CONTEXT.get();
    }
}
